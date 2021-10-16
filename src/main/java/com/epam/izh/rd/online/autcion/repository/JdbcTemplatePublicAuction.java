package com.epam.izh.rd.online.autcion.repository;

import com.epam.izh.rd.online.autcion.entity.Bid;
import com.epam.izh.rd.online.autcion.entity.Item;
import com.epam.izh.rd.online.autcion.entity.User;
import com.epam.izh.rd.online.autcion.mappers.BidMapper;
import com.epam.izh.rd.online.autcion.mappers.ItemMapper;
import com.epam.izh.rd.online.autcion.mappers.UserMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JdbcTemplatePublicAuction implements PublicAuction {

    JdbcTemplate jdbcTemplate;
    BidMapper bidMapper;
    ItemMapper itemMapper;
    UserMapper userMapper;

    public JdbcTemplatePublicAuction(JdbcTemplate jdbcTemplate, BidMapper bidMapper, ItemMapper itemMapper, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.bidMapper = bidMapper;
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
    }

    @Override
    public List<Bid> getUserBids(long id) {
        String sql = "SELECT * FROM bids WHERE user_id = ?";
        return jdbcTemplate.query(sql, bidMapper, id);
    }

    @Override
    public List<Item> getUserItems(long id) {
        String sql = "SELECT * FROM items WHERE user_id = ?";
        return jdbcTemplate.query(sql, itemMapper, id);
    }

    @Override
    public Item getItemByName(String name) {
        String sql = "SELECT * FROM items WHERE title = ?";
        return jdbcTemplate.queryForObject(sql, itemMapper, name);
    }

    @Override
    public Item getItemByDescription(String name) {
        String sql = "SELECT * FROM items WHERE description = ?";
        return jdbcTemplate.queryForObject(sql, itemMapper, name);
    }

    @Override
    public Map<User, Double> getAvgItemCost() {
        String sql = "SELECT user_id, AVG(start_price) FROM items GROUP BY user_id";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        Map<User, Double> avgItemCost = new HashMap<>();

        for (Map<String, Object> map : mapList) {
            avgItemCost.put(
                    jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?", userMapper, map.get("USER_ID")),
                    (Double) map.get("AVG(START_PRICE)"));
        }

        return avgItemCost;
    }

    @Override
    public Map<Item, Bid> getMaxBidsForEveryItem() {
        String sql = "SELECT item_id, MAX(bid_value) FROM bids GROUP BY item_id";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        Map<Item, Bid> maxBidsForEveryItem = new HashMap<>();

        for (Map<String, Object> map : mapList) {
            maxBidsForEveryItem.put(
                    jdbcTemplate.queryForObject("SELECT * FROM items WHERE item_id = ? ", itemMapper, map.get("item_id")),
                    jdbcTemplate.queryForObject("SELECT * FROM bids WHERE bid_value = ? AND item_id = ?", bidMapper, map.get("MAX(bid_value)"), map.get("item_id")));
        }

        return maxBidsForEveryItem;
    }

    @Override
    public boolean createUser(User user) {
        String sql = "INSERT INTO users  VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getUserId(),
                user.getBillingAddress(),
                user.getFullName(),
                user.getLogin(),
                user.getPassword()) != 0;
    }

    @Override
    public boolean createItem(Item item) {
        String sql = "INSERT INTO items values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                item.getItemId(),
                item.getBidIncrement(),
                item.getBuyItNow(),
                item.getDescription(),
                item.getStartDate(),
                item.getStartPrice(),
                item.getStopDate(),
                item.getTitle(),
                item.getUserId()) != 0;
    }

    @Override
    public boolean createBid(Bid bid) {
        String sql = "INSERT INTO bids  values(?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                bid.getBidId(),
                bid.getBidDate(),
                bid.getBidValue(),
                bid.getItemId(),
                bid.getUserId()) != 0;
    }

    @Override
    public boolean deleteUserBids(long id) {
        String sql = "DELETE FROM bids WHERE user_id = ?";
        return jdbcTemplate.update(sql, id) != 0;
    }

    @Override
    public boolean doubleItemsStartPrice(long id) {
        String sql = "UPDATE items SET start_price = start_price * 2 WHERE user_id = ?";
        return jdbcTemplate.update(sql, id) != 0;
    }
}
