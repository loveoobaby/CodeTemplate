package com.yss.enumd;

/**
 * @author yss
 * @date 2019/3/17上午9:23
 * @description: TODO
 */
public class enum_enum_demo {

    private static enum Direction {
        EAST, WEST, NORTH, SOUTH;
    }

    /**
     * 每个枚举值都是一个枚举类的实例，因此上述定义等价于
     *
     * final class Direction extends Enum<Direction>
     * {
     *     public final static Direction EAST = new Direction();
     *     public final static Direction WEST = new Direction();
     *     public final static Direction NORTH = new Direction();
     *     public final static Direction SOUTH = new Direction();
     * }
     *
     */


    /**
     * 枚举可以添加构造方法, 可以定义方法， 甚至是抽象方法
     */
    public enum Direction2 {
        // enum fields
        EAST(0) {
            @Override
            public String printDirection() {
                String message = "You are moving in east. You will face sun in morning time.";
                return message;
            }
        }, WEST(180) {
            @Override
            public String printDirection() {
                String message = "You are moving in west. You will face sun in evening time.";
                return message;
            }
        }, NORTH(90) {
            @Override
            public String printDirection() {
                String message = "You are moving in north. You will face head in daytime.";
                return message;
            }

        }, SOUTH(270) {
            @Override
            public String printDirection() {
                String message = "You are moving in south. Sea ahead.";
                return message;
            }
        };

        // constructor
        private Direction2(final int angle) {
            this.angle = angle;
        }

        // internal state
        private int angle;

        public int getAngle() {
            return angle;
        }

        public abstract String printDirection();
    }


    public static void main(String[] args) {

        // ordinal返回枚举实例的序号
        {
            Direction.EAST.ordinal();     //0
            Direction.NORTH.ordinal();    //2
        }

        // values()返回所有定义的枚举实例数组
        {
            Direction[] directions = Direction.values();

            for (Direction d : directions) {
                System.out.println(d);
            }
        }

        // valueOf()可以将String转换成枚举
        {
            Direction east = Direction.valueOf("EAST");
            System.out.println(east);
        }

        {
            Direction2 north = Direction2.NORTH;
            System.out.println(north);                      //NORTH
            System.out.println(north.getAngle());           //90
            System.out.println(Direction2.NORTH.getAngle()); //90
        }


    }

}
