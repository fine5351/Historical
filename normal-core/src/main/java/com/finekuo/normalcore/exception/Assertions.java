package com.finekuo.normalcore.exception;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 提供鏈式斷言功能的工具類
 */
public class Assertions {

    /**
     * 創建一個布爾表達式斷言
     *
     * @param expression 待評估的布爾表達式
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isTrue(boolean expression) {
        return new BooleanAssertion(expression);
    }

    /**
     * 創建一個對象非空斷言
     *
     * @param object 待評估的對象
     * @return 斷言對象，可以進行後續操作
     */
    public static ObjectAssertion isNotNull(Object object) {
        return new ObjectAssertion(object != null);
    }

    /**
     * 創建一個相等性斷言
     *
     * @param expected 期望值
     * @param actual   實際值
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isEqual(Object expected, Object actual) {
        return new BooleanAssertion(Objects.equals(expected, actual));
    }

    /**
     * 創建一個集合為空的斷言
     *
     * @param collection 待評估的集合
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isEmpty(Collection<?> collection) {
        return new BooleanAssertion(collection == null || collection.isEmpty());
    }

    /**
     * 創建一個集合不為空的斷言
     *
     * @param collection 待評估的集合
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isNotEmpty(Collection<?> collection) {
        return new BooleanAssertion(collection != null && !collection.isEmpty());
    }

    /**
     * 創建一個 Map 為空的斷言
     *
     * @param map 待評估的 Map
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isEmpty(Map<?, ?> map) {
        return new BooleanAssertion(map == null || map.isEmpty());
    }

    /**
     * 創建一個 Map 不為空的斷言
     *
     * @param map 待評估的 Map
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isNotEmpty(Map<?, ?> map) {
        return new BooleanAssertion(map != null && !map.isEmpty());
    }

    /**
     * 創建一個字符串為空的斷言
     *
     * @param str 待評估的字符串
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isEmpty(String str) {
        return new BooleanAssertion(str == null || str.isEmpty());
    }

    /**
     * 創建一個字符串不為空的斷言
     *
     * @param str 待評估的字符串
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isNotEmpty(String str) {
        return new BooleanAssertion(str != null && !str.isEmpty());
    }

    /**
     * 創建一個字符串為空白的斷言
     *
     * @param str 待評估的字符串
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isBlank(String str) {
        return new BooleanAssertion(str == null || str.trim().isEmpty());
    }

    /**
     * 創建一個字符串不為空白的斷言
     *
     * @param str 待評估的字符串
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isNotBlank(String str) {
        return new BooleanAssertion(str != null && !str.trim().isEmpty());
    }

    /**
     * 創建一個數組為空的斷言
     *
     * @param array 待評估的數組
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isEmpty(Object[] array) {
        return new BooleanAssertion(array == null || array.length == 0);
    }

    /**
     * 創建一個大於斷言
     *
     * @param value     待比較的值
     * @param threshold 閾值
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isGreaterThan(int value, int threshold) {
        return new BooleanAssertion(value > threshold);
    }

    /**
     * 創建一個大於等於斷言
     *
     * @param value     待比較的值
     * @param threshold 閾值
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isGreaterThanOrEqual(int value, int threshold) {
        return new BooleanAssertion(value >= threshold);
    }

    /**
     * 創建一個小於斷言
     *
     * @param value     待比較的值
     * @param threshold 閾值
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isLessThan(int value, int threshold) {
        return new BooleanAssertion(value < threshold);
    }

    /**
     * 創建一個小於等於斷言
     *
     * @param value     待比較的值
     * @param threshold 閾值
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isLessThanOrEqual(int value, int threshold) {
        return new BooleanAssertion(value <= threshold);
    }

    /**
     * 創建一個在範圍內的斷言
     *
     * @param value 待評估的值
     * @param min   最小值（包含）
     * @param max   最大值（包含）
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isBetween(int value, int min, int max) {
        return new BooleanAssertion(value >= min && value <= max);
    }

    /**
     * 創建一個數組不為空的斷言
     *
     * @param array 待評估的數組
     * @return 斷言對象，可以進行後續操作
     */
    public static BooleanAssertion isNotEmpty(Object[] array) {
        return new BooleanAssertion(array != null && array.length > 0);
    }

    /**
     * 布爾表達式斷言類
     */
    public static class BooleanAssertion {

        private final boolean result;

        private BooleanAssertion(boolean result) {
            this.result = result;
        }

        /**
         * 如果斷言結果為 false，則拋出指定異常
         *
         * @param exceptionSupplier 異常提供者
         * @param <E>               異常類型
         * @throws E 如果斷言結果為 false
         */
        public <E extends Throwable> void elseThrow(Supplier<E> exceptionSupplier) throws E {
            if (!result) {
                throw exceptionSupplier.get();
            }
        }

        /**
         * 如果斷言結果為 false，則拋出 BusinessException
         *
         * @param code    錯誤碼
         * @param message 錯誤消息
         * @throws BusinessException 如果斷言結果為 false
         */
        public void elseThrowBusiness(String code, String message) {
            if (!result) {
                BusinessException exception = new BusinessException();
                exception.setCode(code);
                exception.setMessage(message);
                throw exception;
            }
        }

        /**
         * 如果斷言結果為 false，則拋出 SystemException
         *
         * @param code    錯誤碼
         * @param message 錯誤消息
         * @throws SystemException 如果斷言結果為 false
         */
        public void elseThrowSystem(String code, String message) {
            if (!result) {
                SystemException exception = new SystemException();
                exception.setCode(code);
                exception.setMessage(message);
                throw exception;
            }
        }

    }

    /**
     * 對象斷言類，繼承自 BooleanAssertion
     */
    public static class ObjectAssertion extends BooleanAssertion {

        private ObjectAssertion(boolean result) {
            super(result);
        }

    }

}
