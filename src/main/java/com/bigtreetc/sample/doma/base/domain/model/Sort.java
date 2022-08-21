package com.bigtreetc.sample.doma.base.domain.model;

import java.io.Serializable;
import java.util.*;
import lombok.Getter;
import lombok.val;
import org.springframework.util.StringUtils;

/** ソート順 */
public class Sort implements Iterable<Sort.Order>, Serializable {

  private static final long serialVersionUID = -7583714602163065779L;

  public static final Sort DEFAULT = Sort.by(new Order[0]);

  private final List<Order> orders;

  /**
   * コンストラクタ
   *
   * @param orders
   */
  public Sort(Order... orders) {
    this.orders = Arrays.asList(orders);
  }

  public Sort(List<Order> orders) {
    Objects.requireNonNull(orders, "orders must not be null.");
    this.orders = new ArrayList<>(orders);
  }

  /**
   * コンストラクタ
   *
   * @param properties
   */
  public Sort(String... properties) {
    this(Direction.ASC, properties);
  }

  /**
   * コンストラクタ
   *
   * @param direction
   * @param properties
   */
  public Sort(Direction direction, String... properties) {
    this(direction, properties == null ? new ArrayList<>() : Arrays.asList(properties));
  }

  /**
   * コンストラクタ
   *
   * @param direction
   * @param properties
   */
  public Sort(Direction direction, List<String> properties) {
    this.orders = new ArrayList<>(properties.size());

    for (String property : properties) {
      this.orders.add(new Order(direction, property));
    }
  }

  /**
   * @param sort
   * @return
   */
  public Sort and(Sort sort) {
    Objects.requireNonNull(sort, "sort must not be null.");
    val orders = new ArrayList<Order>(this.orders);

    for (Order order : sort) {
      orders.add(order);
    }

    return Sort.by(orders);
  }

  /**
   * @param properties
   * @return
   */
  public static Sort by(String... properties) {
    Objects.requireNonNull(properties, "properties must not be null.");
    return properties.length == 0 ? DEFAULT : new Sort(properties);
  }

  /**
   * @param orders
   * @return
   */
  public static Sort by(Order... orders) {
    Objects.requireNonNull(orders, "orders must not be null.");
    return new Sort(orders);
  }

  /**
   * @param orders
   * @return
   */
  public static Sort by(List<Order> orders) {
    Objects.requireNonNull(orders, "orders must not be null.");
    return orders.isEmpty() ? DEFAULT : new Sort(orders);
  }

  /**
   * ソートが指定されている場合はTrueを返します。
   *
   * @return
   */
  public boolean isSorted() {
    return !orders.isEmpty();
  }

  @Override
  public Iterator<Order> iterator() {
    return this.orders.iterator();
  }

  @Override
  public String toString() {
    return orders.isEmpty() ? "" : StringUtils.collectionToCommaDelimitedString(orders);
  }

  /** ソート方向 */
  public enum Direction {
    DESC,
    ASC
  }

  /** ソート方向とプロパティを保持するクラス */
  public static class Order implements Serializable {

    private static final long serialVersionUID = -3242307846096217707L;

    @Getter private final Direction direction;

    @Getter private final String property;

    public Order(Direction direction, String property) {
      this.direction = direction == null ? Direction.ASC : direction;
      this.property = property;
    }

    public String toString() {
      return String.format("%s %s", property, direction);
    }
  }
}
