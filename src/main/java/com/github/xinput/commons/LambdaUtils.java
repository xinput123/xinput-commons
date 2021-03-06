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
   * List<T> ==> List<Field> ??????
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
   * List<T> ==> List<Field> ??????
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
   * ?????? List<T> => Map<String,List<T>>
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
   * ?????? List<T> => Map<String,List<T>>
   * ==> students.stream().filter(s -> s.getAge() < 22).collect(Collectors.groupingBy(Student::getAge));
   *
   * @param collection List<User>
   * @param predicate  ????????????
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
   * ?????????Map
   * List<T> => Map<String,Object>
   * ==> students.stream().collect(Collectors.toMap(Student::getName, k1 -> k1, (k1, k2) -> k2));
   *
   * @param collection    ??????List<User>
   * @param keyMapper     ??????User::getId
   * @param valMapper     ??????User::getName
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
   * ???????????????
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
   * ?????????????????????????????????????????????????????????
   *
   * @param collection ??????????????????
   * @param delimiter  ?????????
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
   * ?????????????????????????????????????????????????????????
   *
   * @param collection ??????????????????
   * @param delimiter  ?????????
   * @param prefix     ????????????
   * @param suffix     ????????????
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
   * ??????????????????????????????????????????
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
   * ??????????????????filter
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
   * ??????????????????filter
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
   * ??????????????????filter
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
   * ??????????????????filter
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

  // ======================?????????=========================

  /**
   * ???????????????????????????????????????
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
   * ???Map???key????????????
   */
  public static <R> Optional<Integer> getMaxIntKey(Map<Integer, R> map) {
    if (MapUtils.isEmpty(map)) {
      return Optional.empty();
    }

    return map.keySet().stream().max(Integer::compareTo);
  }

  /**
   * ???Map???value????????????
   */
  public static <R> Optional<Integer> getMaxIntValue(Map<R, Integer> map) {
    if (MapUtils.isEmpty(map)) {
      return Optional.empty();
    }

    return map.values().stream().max(Integer::compareTo);
  }

  // ?????????????????????????????????
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
   * ???List??????????????????lists.subList(int fromIndex, int toIndex) ??????????????????????????????
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
