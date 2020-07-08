package com.company;

import javax.swing.*;
import java.awt.*;

public class MyBinaryTree<T> {
    private int height = 0;
    private TreeNode<T> root;

    MyBinaryTree() {
        root = null;
    }

    public int height() {
        return height;
    }

    public void add(int key, T value) {
        TreeNode<T> link = root;
        if (link == null) {
            root = new TreeNode<>(key, value, null, null);
        } else {
            int i = 0;
            while (true) {
                i++;
                if (link.left != null && link.key > key) {
                    link = link.left;
                } else if (link.left == null && link.key > key) {
                    link.left = new TreeNode<>(key, value, null, null);
                    if (i > height) {
                        height = i;
                    }
                    break;
                } else if (link.right != null) {
                    link = link.right;
                } else {
                    link.right = new TreeNode<>(key, value, null, null);
                    if (i > height) {
                        height = i;
                    }
                    break;
                }
            }
        }
    }

    public String print() {
        StringBuilder builder = new StringBuilder();
        if (root != null) {
            builder.append(root.key).append("{").append(root.value).append("}").append(" (");
            if (root.left != null) {
                builder.append(root.left.key).append("{").append(root.left.value).append("}");
                print(root.left, builder);
            }
            builder.append(", ");
            if (root.right != null) {
                builder.append(root.right.key).append("{").append(root.right.value).append("}");
                print(root.right, builder);
            }
            builder.append(")");
        }
        return builder.toString();
    }

    private void print(TreeNode<T> link, StringBuilder builder) {
        if (link.left != null || link.right != null) {
            builder.append(" (");
            if (link.left != null) {
                builder.append(link.left.key).append("{").append(link.left.value).append("}");
                print(link.left, builder);
            }
            builder.append(", ");
            if (link.right != null) {
                builder.append(link.right.key).append("{").append(link.right.value).append("}");
                print(link.right, builder);
            }
            builder.append(")");
        }
    }

    public void delete() {
        if (root.left != null && (root.left.right != null || root.left.left != null)) {
            delete(root.left, 2);
        } else if (height != 1) {
            root.left = null;
        }
        if (root.right != null && (root.right.right != null || root.right.left != null)) {
            delete(root.right, 2);
        } else if (height != 1) {
            root.right = null;
        }
    }

    private void delete(TreeNode<T> node, int level) {
        if (node.left != null && (node.left.right != null || node.left.left != null)) {
            delete(node.left, level + 1);
        } else if (level < height) {
            node.left = null;
        }
        if (node.right != null && (node.right.right != null || node.right.left != null)) {
            delete(node.right, level + 1);
        } else if (level < height) {
            node.right = null;
        }
    }

    public boolean contains(T element, int key) {
        TreeNode<T> node = root;
        boolean i = true;
        while (i) {
            i = false;
            if (node != null && node.key < key) {
                node = node.right;
                i = true;
            } else if (node != null && node.key > key) {
                node = node.left;
                i = true;
            } else if (node != null && node.value.equals(element)) {
                return true;
            }
        }
        return false;
    }

    private static class TreeNode<T> {
        int key;
        T value;
        TreeNode<T> left;
        TreeNode<T> right;

        TreeNode(int key, T value, TreeNode<T> left, TreeNode<T> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    private static class TreeNodePaint extends JComponent {
        String value;
        int key;

        TreeNodePaint(int x, int y, int key, String value) {
            this.key = key;
            this.value = value;
            setLocation(x, y);
            setSize(50, 50);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(new Color(255, 255, 255));
            g.fillRect(0, 0, 50, 50);
            g.setColor(new Color(0, 0, 0));
            g.setFont(new Font("Lol", Font.BOLD, 20));
            g.drawString(String.valueOf(key), 25 - String.valueOf(key).length() / 2 * 10, 20);
            g.setFont(new Font("lol", Font.PLAIN, 14));
            g.drawString(value, 25 - value.length() / 2 * 7, 45);
            g.drawLine(0, 25, 50, 25);
            g.drawLine(0, 0, 0, 50);
            g.drawLine(49, 0, 49, 50);
            g.drawLine(0, 0, 49, 0);
            g.drawLine(0, 49, 49, 49);
        }
    }

    public static class TreePaint<T> extends JComponent {
        private final int height;

        TreePaint(MyBinaryTree<T> tree) {
            this.height = tree.height;
            int[] coordinates = new int[tree.height + 1];
            for (int v : coordinates) {
                v = 0;
            }
            paint(tree.root, 0, coordinates);
        }

        int[][][] coordinates() {
            int[][][] coordinates = new int[height + 1][][];
            int[][] coo = new int[(int) Math.pow(2, height)][2];
            for (int i = 0; i < Math.pow(2, height); i++) {
                coo[i][0] = 10 + i * 60;
                coo[i][1] = 60 * height + 10;
            }
            coordinates[height] = coo;
            for (int j = height - 1; j >= 0; j--) {
                int[][] co = new int[(int) Math.pow(2, j)][2];
                for (int i = 0; i < Math.pow(2, j); i++) {
                    co[i][0] = (coordinates[j + 1][2 * i + 1][0] + 50 - coordinates[j + 1][2 * i][0]) / 2 - 25 + coordinates[j + 1][2 * i][0];
                    co[i][1] = 60 * j + 10;
                }
                coordinates[j] = co;
            }
            return coordinates;
        }

        public void paint(TreeNode<T> link, int level, int[] coordinates) {
            TreeNodePaint nodePaint = new TreeNodePaint(
                    coordinates()[level][coordinates[level]][0],
                    coordinates()[level][coordinates[level]++][1],
                    link.key,
                    String.valueOf(link.value)
            );
            add(nodePaint);
            if (link.left != null) {
                paint(link.left, level + 1, coordinates);
            } else {
                for (int i = 0; i < height - level; i++) {
                    for (int j = 0; j < Math.pow(2, i); j++) {
                        coordinates[level + 1 + i]++;
                    }
                }
            }
            if (link.right != null) {
                paint(link.right, level + 1, coordinates);
            } else {
                for (int i = 0; i < height - level; i++) {
                    for (int j = 0; j < Math.pow(2, i); j++) {
                        coordinates[level + 1 + i]++;
                    }
                }
            }
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            int level = 0;
            while (level < height) {
                for (int i = 0; i < Math.pow(2, level); i++) {
                    if (getComponentAt(coordinates()[level][i][0], coordinates()[level][i][1]) != null) {
                        if (getComponentAt(coordinates()[level + 1][2 * i][0], coordinates()[level + 1][2 * i][1]) != null &&
                                getComponentAt(coordinates()[level + 1][2 * i][0], coordinates()[level + 1][2 * i][1]) instanceof TreeNodePaint
                        ) {

                            g.drawLine(
                                    coordinates()[level][i][0] + 25,
                                    coordinates()[level][i][1] + 50,
                                    coordinates()[level + 1][2 * i][0] + 25,
                                    coordinates()[level + 1][2 * i][1]
                            );
                        }
                        if (getComponentAt(coordinates()[level + 1][2 * i + 1][0], coordinates()[level + 1][2 * i + 1][1]) != null &&
                                getComponentAt(coordinates()[level + 1][2 * i + 1][0], coordinates()[level + 1][2 * i + 1][1]) instanceof TreeNodePaint
                        ) {
                            g.drawLine(
                                    coordinates()[level][i][0] + 25,
                                    coordinates()[level][i][1] + 50,
                                    coordinates()[level + 1][2 * i + 1][0] + 25,
                                    coordinates()[level + 1][2 * i + 1][1]
                            );
                        }
                    }
                }
                level++;
            }
        }
    }
}
