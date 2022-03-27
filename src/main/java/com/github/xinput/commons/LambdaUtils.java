package com.github.xinput.commons;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:xinput.xx@gmail.com">xinput</a>
 * @version 1.0
 */
public class LambdaUtils {

  /**
   * List<T> ==> List<Field>
   * ==> students.stream().map(Student::getName).collect(Collectors.toList());
   *
   * @param collection List<User>
   * @param function   User::getName
   */
  public static <R, A> List<A> collectColumn(Collection<R> collection,
                                             Function<R, A> function) {
    if (CollectionUtils.isEmpty(collection)) {
      return Lists.newArrayList();
    }

    return collection.stream()
        .map(function)
        .collect(Collectors.toList());
  }

  /**
   * List<T> ==> List<Field>
   * ==> students.stream().map(Student::getName).filter(user -> user.getAge()>10).collect(Collectors.toList());
   *
   * @param collection List<User>
   * @param predicate  user -> user.getAge()>10,
   * @param function   User::getName
   */
  public static <R, A> List<A> collectColumn(Collection<R> collection,
                                             Predicate<R> predicate,
                                             Function<R, A> function) {
    if (CollectionUtils.isEmpty(collection)) {
      return Lists.newArrayList();
    }

    return collection.stream()
        .filter(predicate)
        .map(function)
        .collect(Collectors.toList());
  }

  /**
   * List<T> ==> List<Field> 去重
   * ==> students.stream().map(Student::getAge).distinct().collect(Collectors.toList())
   *
   * @param collection List<User>
   * @param function   User::getName
   */
  public static <R, A> List<A> collectDistinctColumn(Collection<R> collection,
                                                     Function<R, A> function) {
    if (CollectionUtils.isEmpty(collection)) {
      return Lists.newArrayList();
    }

    return collection.stream()
        .map(function)
        .distinct()
        .collect(Collectors.toList());
  }

  /**
   * List<T> ==> List<Field> 去重
   *
   * @param collection List<User>
   * @param predicate  -> user.getAge()>10,
   * @param function   User::getName
   */
  public static <R, A> List<A> collectDistinctColumn(Collection<R> collection,
                                                     Predicate<R> predicate,
                                                     Function<R, A> function) {
    if (CollectionUtils.isEmpty(collection)) {
      return Lists.newArrayList();
    }

    return collection.stream()
        .filter(predicate)
        .map(function)
        .distinct()
        .collect(Collectors.toList());
  }

  /**
   * 分组 List<T> => Map<String,List<T>>
   * ==> students.stream().collect(Collectors.groupingBy(Student::getAge));
   *
   * @param collection List<User>
   * @param function   User::getId
   */
  public static <R, A> Map<A, List<R>> group(Collection<R> collection,
                                             Function<R, A> function) {
    if (CollectionUtils.isEmpty(collection)) {
      return Maps.newHashMap();
    }

    return collection.stream()
        .collect(Collectors.groupingBy(function));
  }

  /**
   * 分组 List<T> => Map<String,List<T>>
   * ==> students.stream().filter(s -> s.getAge() < 22).collect(Collectors.groupingBy(Student::getAge));
   *
   * @param collection List<User>
   * @param predicate  筛选条件
   * @param function   User::getId
   */
  public static <R, A> Map<A, List<R>> group(Collection<R> collection,
                                             Predicate<R> predicate,
                                             Function<R, A> function) {
    if (CollectionUtils.isEmpty(collection)) {
      return Maps.newHashMap();
    }

    return collection.stream()
        .filter(predicate)
        .collect(Collectors.groupingBy(function));
  }

  public static <R, A> Map<A, List<R>> group(Collection<R> collection,
                                             Predicate<R> predicate1,
                                             Predicate<R> predicate2,
                                             Function<R, A> function) {
    if (CollectionUtils.isEmpty(collection)) {
      return Maps.newHashMap();
    }

    return collection.stream()
        .filter(predicate1)
        .filter(predicate2)
        .collect(Collectors.groupingBy(function));
  }

  public static <R, A> Map<A, List<R>> group(Collection<R> collection,
                                             Predicate<R> predicate1,
                                             Predicate<R> predicate2,
                                             Predicate<R> predicate3,
                                             Function<R, A> function) {
    if (CollectionUtils.isEmpty(collection)) {
      return Maps.newHashMap();
    }

    return collection.stream()
        .filter(predicate1)
        .filter(predicate2)
        .filter(predicate3)
        .collect(Collectors.groupingBy(function));
  }

  /**
   * 集合转Map
   * List<T> => Map<String,Object>
   * ==> students.stream().collect(Collectors.toMap(Student::getName, k1 -> k1, (k1, k2) -> k2));
   *
   * @param collection    集合List<User>
   * @param keyMapper     字段User::getId
   * @param valMapper     字段User::getName
   * @param mergeFunction (k2, k1) -> k2
   */
  public static <R, K, V> Map<K, V> toMap(Collection<R> collection,
                                          Function<R, K> keyMapper,
                                          Function<R, V> valMapper,
                                          BinaryOperator<V> mergeFunction) {
    if (CollectionUtils.isEmpty(collection)) {
      return Maps.newHashMap();
    }

    return collection.stream()
        .collect(
            Collectors.toMap(
                keyMapper,
                valMapper,
                mergeFunction,
                () -> Maps.newHashMapWithExpectedSize(collection.size())
            )
        );
  }

  public static <R, K, V> Map<K, V> toMap(Collection<R> collection,
                                          Predicate<R> predicate,
                                          Function<R, K> keyMapper,
                                          Function<R, V> valMapper,
                                          BinaryOperator<V> mergeFunction) {
    if (CollectionUtils.isEmpty(collection)) {
      return Maps.newHashMap();
    }

    return collection.stream()
        .filter(predicate)
        .collect(
            Collectors.toMap(keyMapper,
                valMapper,
                mergeFunction,
                () -> Maps.newHashMapWithExpectedSize(collection.size())
            )
        );
  }

  public static <R, K, V> Map<K, V> toMap(Collection<R> collection,
                                          Predicate<R> predicate1,
                                          Predicate<R> predicate2,
                                          Function<R, K> keyMapper,
                                          Function<R, V> valMapper,
                                          BinaryOperator<V> mergeFunction) {
    if (CollectionUtils.isEmpty(collection)) {
      return Maps.newHashMap();
    }

    return collection.stream()
        .filter(predicate1)
        .filter(predicate2)
        .collect(
            Collectors.toMap(keyMapper,
                valMapper,
                mergeFunction,
                () -> Maps.newHashMapWithExpectedSize(collection.size())
            )
        );
  }

  /**
   * 拼接字符串
   */
  public static String union(Collection<String> collection) {
    if (CollectionUtils.isEmpty(collection)) {
      return StringUtils.EMPTY;
    }

    return collection.stream().collect(Collectors.joining(","));
  }

  public static String union(Collection<String> collection, Predicate<String> predicate) {
    if (CollectionUtils.isEmpty(collection)) {
      return StringUtils.EMPTY;
    }
    return collection.stream().filter(predicate).collect(Collectors.joining(","));
  }

  /**
   * 使用指定连接符将集合中的参数拼接在一起
   *
   * @param collection 指定集合数据
   * @param delimiter  拼接符
   */
  public static String union(Collection<String> collection, String delimiter) {
    if (CollectionUtils.isEmpty(collection)) {
      return StringUtils.EMPTY;
    }
    return collection.stream().collect(Collectors.joining(delimiter));
  }

  public static String union(Collection<String> collection,
                             Predicate<String> predicate,
                             String delimiter) {
    if (CollectionUtils.isEmpty(collection)) {
      return StringUtils.EMPTY;
    }
    return collection.stream().filter(predicate)
        .collect(Collectors.joining(delimiter));
  }

  /**
   * 使用指定连接符将集合中的参数拼接在一起
   *
   * @param collection 指定集合数据
   * @param delimiter  拼接符
   * @param prefix     前缀符号
   * @param suffix     后缀符号
   */
  public static String union(Collection<String> collection,
                             String delimiter,
                             String prefix,
                             String suffix) {
    if (CollectionUtils.isEmpty(collection)) {
      return StringUtils.EMPTY;
    }
    return collection.stream()
        .collect(Collectors.joining(delimiter, prefix, suffix));
  }

  /**
   * 将集合中的所有非空字符串拼接
   *
   * @param collection
   * @param delimiter
   */
  public static String unionNotEmptyString(Collection<String> collection, String delimiter) {
    if (CollectionUtils.isEmpty(collection)) {
      return StringUtils.EMPTY;
    }

    return collection.stream()
        .filter(col -> StringUtils.isNotBlank(col))
        .collect(Collectors.joining(delimiter));
  }

  public static String unionNotEmptyString(Collection<String> collection,
                                           String delimiter,
                                           String prefix,
                                           String suffix) {
    if (CollectionUtils.isEmpty(collection)) {
      return StringUtils.EMPTY;
    }

    return collection.stream()
        .filter(col -> StringUtils.isNotBlank(col))
        .collect(Collectors.joining(delimiter, prefix, suffix));
  }

  /**
   * 根据条件筛选filter
   */
  public static <R> List<R> filter(Collection<R> collection, Predicate<R> predicate) {
    if (CollectionUtils.isEmpty(collection)) {
      return Lists.newArrayList();
    }

    return collection.stream()
        .filter(predicate)
        .collect(Collectors.toList());
  }

  /**
   * 根据条件筛选filter
   */
  public static <R> List<R> filter(Collection<R> collection, Predicate<R>... predicates) {
    if (CollectionUtils.isEmpty(collection)) {
      return Lists.newArrayList();
    }

    Stream<R> stream = collection.stream();
    for (Predicate<R> predicate : predicates) {
      stream = stream.filter(predicate);
    }
    return stream.collect(Collectors.toList());
  }

  /**
   * 根据条件筛选filter
   */
  public static <R> Set<R> filterSet(Collection<R> collection, Predicate<R> predicate) {
    if (CollectionUtils.isEmpty(collection)) {
      return Sets.newHashSet();
    }

    return collection.stream()
        .filter(predicate)
        .collect(Collectors.toSet());
  }

  /**
   * 根据条件筛选filter
   */
  public static <R> Set<R> filterSet(Collection<R> collection, Predicate<R>... predicates) {
    if (CollectionUtils.isEmpty(collection)) {
      return Sets.newHashSet();
    }

    Stream<R> stream = collection.stream();
    for (Predicate<R> predicate : predicates) {
      stream = stream.filter(predicate);
    }
    return stream.collect(Collectors.toSet());
  }

  // ======================取最值=========================

  /**
   * 获取集合中某个字段的最小值
   *
   * @param collection List<Channel>
   * @param function   Channel::getStarMinLevel
   */
  public static <R> int getMin(Collection<R> collection,
                               ToIntFunction<R> function) {
    return getMin(collection, function, 0);
  }

  public static <R> int getMin(Collection<R> collection,
                               ToIntFunction<R> function,
                               int defaultValue) {
    if (CollectionUtils.isEmpty(collection)) {
      return defaultValue;
    }

    return collection.stream().mapToInt(function).min().orElse(defaultValue);
  }

  public static <R> long getLongMin(Collection<R> collection,
                                    ToLongFunction<R> function) {
    return getLongMin(collection, function, 0);
  }

  public static <R> long getLongMin(Collection<R> collection,
                                    ToLongFunction<R> function,
                                    long defaultValue) {
    if (CollectionUtils.isEmpty(collection)) {
      return defaultValue;
    }

    return collection.stream().mapToLong(function).min().orElse(defaultValue);
  }

  public static <R> int getMax(Collection<R> collection,
                               ToIntFunction<R> function) {
    return getMax(collection, function, 0);
  }

  public static <R> int getMax(Collection<R> collection,
                               ToIntFunction<R> function,
                               int defaultValue) {
    if (CollectionUtils.isEmpty(collection)) {
      return defaultValue;
    }

    return collection.stream().mapToInt(function).max().orElse(defaultValue);
  }

  public static <R> long getLongMax(Collection<R> collection,
                                    ToLongFunction<R> function) {
    return getLongMax(collection, function, 0);
  }

  public static <R> long getLongMax(Collection<R> collection,
                                    ToLongFunction<R> function,
                                    long defaultValue) {
    if (CollectionUtils.isEmpty(collection)) {
      return defaultValue;
    }

    return collection.stream().mapToLong(function).max().orElse(defaultValue);
  }

  /**
   * 取Map中key的最大值
   */
  public static <R> Optional<Integer> getMaxIntKey(Map<Integer, R> map) {
    if (MapUtils.isEmpty(map)) {
      return Optional.empty();
    }

    return map.keySet().stream().max(Integer::compareTo);
  }

  /**
   * 取Map中value的最大值
   */
  public static <R> Optional<Integer> getMaxIntValue(Map<R, Integer> map) {
    if (MapUtils.isEmpty(map)) {
      return Optional.empty();
    }

    return map.values().stream().max(Integer::compareTo);
  }

  // 计算集合中某个字段的和
  public static <R> long sum(Collection<R> collection,
                             ToLongFunction<R> function) {
    return sum(collection, function, 0L);
  }

  public static <R> long sum(Collection<R> collection,
                             ToLongFunction<R> function,
                             long defaultValue) {
    if (CollectionUtils.isEmpty(collection)) {
      return defaultValue;
    }

    return collection.stream().mapToLong(function).sum();
  }

  public static <R> double sum(Collection<R> collection,
                               ToDoubleFunction<R> function) {
    return sum(collection, function, 0D);
  }

  public static <R> double sum(Collection<R> collection,
                               ToDoubleFunction<R> function,
                               double defaultValue) {
    if (CollectionUtils.isEmpty(collection)) {
      return defaultValue;
    }

    return collection.stream().mapToDouble(function).sum();
  }

  /**
   * 取List集合的子集，lists.subList(int fromIndex, int toIndex) 需要判断下标，很麻烦
   */
  public static <A> List<A> sub(List<A> lists, int offset, int size) {
    if (CollectionUtils.isEmpty(lists)) {
      return Lists.newArrayList();
    }

    return lists.stream()
        .skip(offset < 0 ? 0 : offset)
        .limit(size < 0 ? 0 : size)
        .collect(Collectors.toList());
  }

  public static <K, V> Map<K, V> filter(Map<K, V> map, Predicate<? super Map.Entry<K, V>>... predicates) {
    if (MapUtils.isEmpty(map)) {
      return Maps.newHashMap();
    }

    if (predicates == null || predicates.length == 0) {
      return map;
    }

    Stream<Map.Entry<K, V>> entryStream = map.entrySet().stream();
    for (Predicate<? super Map.Entry<K, V>> predicate : predicates) {
      entryStream = entryStream.filter(predicate);
    }

    return entryStream.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
  }
}
