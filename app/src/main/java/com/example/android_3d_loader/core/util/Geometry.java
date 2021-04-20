package com.example.android_3d_loader.core.util;

/**
 * 几何类
 */
public class Geometry {

    /**
     * 点
     */
    public static class Point{
        public final float x, y ,z;

        /**
         * 点
         * @param x
         * @param y
         * @param z
         */
        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * 点向Y轴移
         * @param distance
         * @return
         */
        public Point translateY(float distance){
            return new Point(x, y + distance, z);
        }

        /**
         * 计算空间向量另一头的点
         * @param vector
         * @return
         */
        public Point translate(Vector vector){
            return new Point(
                    this.x + vector.x,
                    this.y + vector.y,
                    this.z + vector.z
            );
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }

    /**
     * 圆
     */
    public static class Circle{
        public final Point center;
        public final float radius;

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        public Circle scale(float scale){
            return new Circle(center, radius * scale);
        }
    }

    /**
     * 圆柱
     */
    public static class Cylinder{
        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }

    /**
     * 正方形
     */
    public static class Square{
        public final Point center;
        public final float width;

        public Square(Point center, float width) {
            this.center = center;
            this.width = width;
        }
    }

    /**
     * 射线
     */
    public static class Ray{
        public final Point point;
        public final Vector vector;

        /**
         * 射线
         * @param point 射线起点
         * @param vector 向量
         */
        public Ray(Point point, Vector vector) {
            this.point = point;
            this.vector = vector;
        }
    }

    public static class Vector2D{
        public float x;
        public float y;

        public Vector2D(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float[] getFloats(){
            return new float[]{
                    this.x,
                    this.y
            };
        }

        public Vector2D plus(Vector2D by){
            return new Vector2D(
                    this.x + by.x,
                    this.y + by.y
            );
        }

        public Vector2D minus(Vector2D by){
            return new Vector2D(
                    this.x - by.x,
                    this.y - by.y
            );
        }

        @Override
        public String toString() {
            return "Vector2D{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    /**
     * 空间向量
     */
    public static class Vector{
        public float x;
        public float y;
        public float z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vector(float[] xyz){
            this.x = xyz[0];
            this.y = xyz[1];
            this.z = xyz[2];
        }

        public float[] getFloats(){
            return new float[]{
                    this.x,
                    this.y,
                    this.z
            };
        }

        public Vector plus(Vector by){
            return new Vector(
                    this.x + by.x,
                    this.y + by.y,
                    this.z + by.z
            );
        }

        public Vector minus(Vector by){
            return new Vector(
                    this.x - by.x,
                    this.y - by.y,
                    this.z - by.z
            );
        }

        public Vector time(float by){
            return new Vector(
                    this.x * by,
                    this.y * by,
                    this.z * by
            );
        }

        @Override
        public String toString() {
            return "Vector{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }

        public Point toPoint(){
            return new Point(x, y, z);
        }

        /**
         * 由两点求出空间向量(末点减起点)
         * @param from 起点
         * @param to 末点
         * @return 空间向量
         */
        public static Vector vectorBetween(Point from, Point to){
            return new Vector(
                    to.x - from.x,
                    to.y - from.y,
                    to.z - from.z
            );
        }

        public static float distanceBetween(Vector from, Vector to){
            return vectorBetween(from.toPoint(), to.toPoint()).length();
        }

        /**
         * 空间向量叉乘
         * @param other 另一个向量
         * @return
         */
        public Vector crossProduct(Vector other) {
            return new Vector(
                    (this.y * other.z) - (this.z * other.y),
                    (this.z * other.x) - (this.x * other.z),
                    (this.x * other.y) - (this.y * other.x)
            );
        }

        /**
         * 空间向量点乘
         * @param other 另一个向量
         * @return
         */
        public float dotProduct(Vector other) {
//            if (this.length() == 0 || other.length() == 0){
//                return 0.0f;
//            }
            return (this.x * other.x + this.y * other.y + this.z * other.z) / (this.length() * other.length());
        }

        /**
         * 空间向量
         * @param f
         * @return
         */
        public Vector scale(float f){
            return new Vector(
                    x * f,
                    y * f,
                    z * f
            );
        }

        /**
         * 空间向量的长度（勾股定理）
         * @return
         */
        public float length(){
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        /**
         * 单位化向量（待验证）
         * @return Vector
         */
        public Vector normalize(){
            float length = this.length();
            return new Vector(
                    x / length,
                    y / length,
                    z / length
            );
        }
    }

    /**
     * 球
     */
    public static class Sphere{
        public final Point center;
        public final float radius;

        public Sphere(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }
    }

    /**
     * 平面
     */
    public static class Plane{
        public final Point point;
        public final Vector normal;

        /**
         * 平面
         * @param point 点
         * @param normal 法向量
         */
        public Plane(Point point, Vector normal) {
            this.point = point;
            this.normal = normal;
        }
    }

    /**
     * 判断射线与球是否相交
     * @param sphere 球
     * @param ray 射线
     * @return true - 相交; false - 不相交
     */
    public static boolean intersects(Sphere sphere, Ray ray) {
        return distanceBetween(sphere.center, ray) < sphere.radius;
    }

    /**
     * 获取射线与平面的交点
     * @param ray 射线
     * @param plane 平面
     * @return 交点
     */
    public static Point intersectionPoint(Ray ray, Plane plane){
        Vector rayToPlaneVector = Vector.vectorBetween(ray.point, plane.point);

        float scaleFactor = rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal);

        Point intersectionPointPoint = ray.point.translate(ray.vector.scale(scaleFactor));

        return intersectionPointPoint;
    }

    /**
     * 点与线之间距离
     * @param point
     * @param ray
     * @return
     */
    private static float distanceBetween(Point point, Ray ray) {
        Vector p1ToPoint = Vector.vectorBetween(ray.point, point);
        Vector p2ToPoint = Vector.vectorBetween(ray.point.translate(ray.vector), point);

        // 求出三角形面积（向量交叉相乘的乘积的长度正好是三角形面积的两倍）
        float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
        // 获取三角形的底
        float lengthOfBase = ray.vector.length();

        // 获取三角形的高
        float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;

        return distanceFromPointToRay;
    }
}
