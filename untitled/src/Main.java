import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
// Класс узла бинарного дерева
class TreeNode {
    int value;
    TreeNode left;
    TreeNode right;
    TreeNode parent;

    public TreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

// Бинарное дерево поиска
class BinarySearchTree {
    TreeNode root;
    protected int nodeCount;

    public BinarySearchTree() {
        root = null;
        nodeCount = 0;
    }

    @Override
    public String toString() {
        return buildTreeString(root, "Root: ", 1);
    }

    private String buildTreeString(TreeNode node, String prefix, int depth) {
        if (node == null) {
            return "Empty";
        }

        StringBuilder result = new StringBuilder(prefix + node);

        StringBuilder indentBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indentBuilder.append("| ");
        }
        String indent = indentBuilder.toString();

        if (node.left != null && node.right == null) {
            result.append("\n").append(indent).append("L: ")
                    .append(buildTreeString(node.left, "", depth + 1));
            result.append("\n").append(indent).append("R: null");
        } else if (node.left == null && node.right != null) {
            result.append("\n").append(indent).append("L: null");
            result.append("\n").append(indent).append("R: ")
                    .append(buildTreeString(node.right, "", depth + 1));
        } else if (node.left != null) {
            result.append("\n").append(indent).append("L: ")
                    .append(buildTreeString(node.left, "", depth + 1));
            result.append("\n").append(indent).append("R: ")
                    .append(buildTreeString(node.right, "", depth + 1));
        }

        return result.toString();
    }

    protected TreeNode createNewNode(int value) {
        return new TreeNode(value);
    }

    public TreeNode insertValue(int value) {
        return insertValue(value, true);
    }

    public TreeNode insertValue(int value, boolean allowDuplicates) {
        if (!allowDuplicates && locateNode(value) != null) {
            return null;
        }

        TreeNode newNode = createNewNode(value);

        if (root == null) {
            root = newNode;
            nodeCount++;
            return newNode;
        }

        TreeNode current = root;
        TreeNode parent = null;

        while (current != null) {
            parent = current;
            if (value < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        newNode.parent = parent;
        if (value < parent.value) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        nodeCount++;
        return newNode;
    }

    public TreeNode locateNode(int value) {
        return locateNodeRecursive(root, value);
    }

    private TreeNode locateNodeRecursive(TreeNode node, int value) {
        if (node == null) {
            return null;
        }

        if (node.value == value) {
            return node;
        } else if (value < node.value) {
            return locateNodeRecursive(node.left, value);
        } else {
            return locateNodeRecursive(node.right, value);
        }
    }

    public TreeNode findSmallest() {
        if (root == null) return null;
        TreeNode node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public TreeNode findLargest() {
        if (root == null) return null;
        TreeNode node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    public TreeNode removeValue(int value) {
        TreeNode node = locateNode(value);
        if (node != null) {
            root = removeRecursive(root, value);
            nodeCount--;
            return node;
        }
        return null;
    }

    private TreeNode removeRecursive(TreeNode node, int value) {
        if (node == null) {
            return null;
        }

        if (value < node.value) {
            node.left = removeRecursive(node.left, value);
            if (node.left != null) {
                node.left.parent = node;
            }
        } else if (value > node.value) {
            node.right = removeRecursive(node.right, value);
            if (node.right != null) {
                node.right.parent = node;
            }
        } else {
            // Узел найден
            if (node.left == null && node.right == null) {
                // Лист
                return null;
            } else if (node.left == null) {
                // Только правый потомок
                node.right.parent = node.parent;
                return node.right;
            } else if (node.right == null) {
                // Только левый потомок
                node.left.parent = node.parent;
                return node.left;
            } else {
                // Два потомка
                TreeNode successor = findMinNode(node.right);
                node.value = successor.value;
                node.right = removeRecursive(node.right, successor.value);
                if (node.right != null) {
                    node.right.parent = node;
                }
            }
        }

        return node;
    }

    private TreeNode findMinNode(TreeNode node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    public int computeHeight(TreeNode node) {
        if (node == null) {
            node = root;
            if (node == null) {
                return 0;
            }
        }

        java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
        queue.add(node);
        int height = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            height++;

            for (int i = 0; i < levelSize; i++) {
                TreeNode current = queue.poll();
                if (current != null) {
                    if (current.left != null) {
                        queue.add(current.left);
                    }
                    if (current.right != null) {
                        queue.add(current.right);
                    }
                }
            }
        }

        return height;
    }

    public java.util.List<Integer> preOrderTraversal(TreeNode node) {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        preOrderRecursive(node, result);
        return result;
    }

    private void preOrderRecursive(TreeNode node, java.util.List<Integer> result) {
        if (node != null) {
            result.add(node.value);
            preOrderRecursive(node.left, result);
            preOrderRecursive(node.right, result);
        }
    }

    public java.util.List<Integer> inOrderTraversal(TreeNode node) {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        inOrderRecursive(node, result);
        return result;
    }

    private void inOrderRecursive(TreeNode node, java.util.List<Integer> result) {
        if (node != null) {
            inOrderRecursive(node.left, result);
            result.add(node.value);
            inOrderRecursive(node.right, result);
        }
    }

    public java.util.List<Integer> postOrderTraversal(TreeNode node) {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        postOrderRecursive(node, result);
        return result;
    }

    private void postOrderRecursive(TreeNode node, java.util.List<Integer> result) {
        if (node != null) {
            postOrderRecursive(node.left, result);
            postOrderRecursive(node.right, result);
            result.add(node.value);
        }
    }

    public java.util.List<Integer> levelOrderTraversal() {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        if (root == null) {
            return result;
        }

        java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            result.add(node.value);

            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        return result;
    }

    public boolean validateBST() {
        return validateBSTRecursive(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean validateBSTRecursive(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.value <= min || node.value >= max) return false;
        return validateBSTRecursive(node.left, min, node.value) &&
                validateBSTRecursive(node.right, node.value, max);
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void clear() {
        root = null;
        nodeCount = 0;
    }
}

// AVL-дерево
class AVLTreeNode extends TreeNode {
    int height;

    public AVLTreeNode(int value) {
        super(value);
        this.height = 1;
    }
}

class AVLTree extends BinarySearchTree {
    @Override
    protected TreeNode createNewNode(int value) {
        return new AVLTreeNode(value);
    }

    private int getNodeHeight(TreeNode node) {
        return node == null ? 0 : ((AVLTreeNode) node).height;
    }

    private void updateHeight(TreeNode node) {
        if (node != null) {
            int leftHeight = getNodeHeight(node.left);
            int rightHeight = getNodeHeight(node.right);
            ((AVLTreeNode) node).height = Math.max(leftHeight, rightHeight) + 1;
        }
    }

    private int getBalanceFactor(TreeNode node) {
        return node == null ? 0 : getNodeHeight(node.left) - getNodeHeight(node.right);
    }

    private TreeNode rotateRight(TreeNode y) {
        TreeNode x = y.left;
        TreeNode T2 = x.right;

        x.right = y;
        y.left = T2;

        if (T2 != null) {
            T2.parent = y;
        }

        x.parent = y.parent;
        y.parent = x;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private TreeNode rotateLeft(TreeNode x) {
        TreeNode y = x.right;
        TreeNode T2 = y.left;

        y.left = x;
        x.right = T2;

        if (T2 != null) {
            T2.parent = x;
        }

        y.parent = x.parent;
        x.parent = y;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private TreeNode balanceNode(TreeNode node) {
        if (node == null) return null;

        updateHeight(node);
        int balance = getBalanceFactor(node);

        // Left Left Case
        if (balance > 1 && getBalanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left Right Case
        if (balance > 1 && getBalanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case
        if (balance < -1 && getBalanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right Left Case
        if (balance < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    @Override
    public TreeNode insertValue(int value, boolean allowDuplicates) {
        if (!allowDuplicates && locateNode(value) != null) {
            return null;
        }

        root = insertRecursive(root, value);
        if (root != null) {
            root.parent = null;
        }
        nodeCount++;
        return locateNode(value);
    }

    private TreeNode insertRecursive(TreeNode node, int value) {
        if (node == null) {
            return createNewNode(value);
        }

        if (value < node.value) {
            node.left = insertRecursive(node.left, value);
            if (node.left != null) node.left.parent = node;
        } else if (value > node.value) {
            node.right = insertRecursive(node.right, value);
            if (node.right != null) node.right.parent = node;
        } else {
            return node;
        }

        return balanceNode(node);
    }

    @Override
    public TreeNode removeValue(int value) {
        TreeNode node = locateNode(value);
        if (node != null) {
            root = removeRecursive(root, value);
            if (root != null) {
                root.parent = null;
            }
            nodeCount--;
            return node;
        }
        return null;
    }

    private TreeNode removeRecursive(TreeNode node, int value) {
        if (node == null) return null;

        if (value < node.value) {
            node.left = removeRecursive(node.left, value);
            if (node.left != null) node.left.parent = node;
        } else if (value > node.value) {
            node.right = removeRecursive(node.right, value);
            if (node.right != null) node.right.parent = node;
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left == null) ? node.right : node.left;
            } else {
                TreeNode minNode = findMinNode(node.right);
                node.value = minNode.value;
                node.right = removeRecursive(node.right, minNode.value);
                if (node.right != null) node.right.parent = node;
            }
        }

        if (node == null) return null;

        return balanceNode(node);
    }

    private TreeNode findMinNode(TreeNode node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    public boolean validateAVL() {
        return validateAVLRecursive(root);
    }

    private boolean validateAVLRecursive(TreeNode node) {
        if (node == null) return true;

        int balance = getBalanceFactor(node);
        if (Math.abs(balance) > 1) {
            return false;
        }

        return validateAVLRecursive(node.left) && validateAVLRecursive(node.right);
    }
}

// Красно-черное дерево
class RBTreeNode extends TreeNode {
    enum Color { RED, BLACK }

    Color color;

    public RBTreeNode(int value) {
        super(value);
        this.color = Color.RED;
    }

    @Override
    public String toString() {
        return value + "(" + (color == Color.RED ? "R" : "B") + ")";
    }
}

class RedBlackTree extends BinarySearchTree {
    private static final RBTreeNode.Color RED = RBTreeNode.Color.RED;
    private static final RBTreeNode.Color BLACK = RBTreeNode.Color.BLACK;

    @Override
    protected TreeNode createNewNode(int value) {
        return new RBTreeNode(value);
    }

    private boolean isRed(TreeNode node) {
        return node != null && ((RBTreeNode) node).color == RED;
    }

    private boolean isBlack(TreeNode node) {
        return node == null || ((RBTreeNode) node).color == BLACK;
    }

    private void setColor(TreeNode node, RBTreeNode.Color color) {
        if (node != null) {
            ((RBTreeNode) node).color = color;
        }
    }

    private TreeNode rotateLeft(TreeNode h) {
        if (h == null || h.right == null) return h;

        TreeNode x = h.right;
        h.right = x.left;
        if (x.left != null) {
            x.left.parent = h;
        }

        x.left = h;
        x.parent = h.parent;
        h.parent = x;

        if (x.parent != null) {
            if (x.parent.left == h) {
                x.parent.left = x;
            } else {
                x.parent.right = x;
            }
        }

        if (root == h) {
            root = x;
        }

        return x;
    }

    private TreeNode rotateRight(TreeNode h) {
        if (h == null || h.left == null) return h;

        TreeNode x = h.left;
        h.left = x.right;
        if (x.right != null) {
            x.right.parent = h;
        }

        x.right = h;
        x.parent = h.parent;
        h.parent = x;

        if (x.parent != null) {
            if (x.parent.left == h) {
                x.parent.left = x;
            } else {
                x.parent.right = x;
            }
        }

        if (root == h) {
            root = x;
        }

        return x;
    }

    @Override
    public TreeNode insertValue(int value, boolean allowDuplicates) {
        if (!allowDuplicates && locateNode(value) != null) {
            return null;
        }

        TreeNode newNode = createNewNode(value);

        if (root == null) {
            root = newNode;
            setColor(root, BLACK);
            nodeCount++;
            return newNode;
        }

        TreeNode current = root;
        TreeNode parent = null;

        while (current != null) {
            parent = current;
            if (value < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        newNode.parent = parent;
        if (value < parent.value) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        fixInsertion(newNode);

        nodeCount++;
        return newNode;
    }

    private void fixInsertion(TreeNode node) {
        TreeNode current = node;

        while (current != root && isRed(current.parent)) {
            TreeNode parent = current.parent;
            TreeNode grandparent = parent.parent;

            if (grandparent == null) break;

            if (parent == grandparent.left) {
                TreeNode uncle = grandparent.right;

                if (isRed(uncle)) {
                    setColor(parent, BLACK);
                    setColor(uncle, BLACK);
                    setColor(grandparent, RED);
                    current = grandparent;
                } else {
                    if (current == parent.right) {
                        current = parent;
                        rotateLeft(current);
                        parent = current.parent;
                        grandparent = parent.parent;
                    }

                    setColor(parent, BLACK);
                    setColor(grandparent, RED);
                    rotateRight(grandparent);
                }
            } else {
                TreeNode uncle = grandparent.left;

                if (isRed(uncle)) {
                    setColor(parent, BLACK);
                    setColor(uncle, BLACK);
                    setColor(grandparent, RED);
                    current = grandparent;
                } else {
                    if (current == parent.left) {
                        current = parent;
                        rotateRight(current);
                        parent = current.parent;
                        grandparent = parent.parent;
                    }

                    setColor(parent, BLACK);
                    setColor(grandparent, RED);
                    rotateLeft(grandparent);
                }
            }
        }

        setColor(root, BLACK);
    }

    @Override
    public TreeNode removeValue(int value) {
        TreeNode node = locateNode(value);
        if (node == null) {
            return null;
        }

        root = removeRecursive(root, value);
        if (root != null) {
            setColor(root, BLACK);
            root.parent = null;
        }
        nodeCount--;

        return node;
    }

    private TreeNode removeRecursive(TreeNode node, int value) {
        if (node == null) return null;

        if (value < node.value) {
            node.left = removeRecursive(node.left, value);
            if (node.left != null) node.left.parent = node;
        } else if (value > node.value) {
            node.right = removeRecursive(node.right, value);
            if (node.right != null) node.right.parent = node;
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left == null) ? node.right : node.left;
            } else {
                TreeNode minNode = findMinNode(node.right);
                node.value = minNode.value;
                node.right = removeRecursive(node.right, minNode.value);
                if (node.right != null) node.right.parent = node;
            }
        }

        return node;
    }

    private TreeNode findMinNode(TreeNode node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    public boolean validateRB() {
        if (root == null) return true;

        if (isRed(root)) {
            return false;
        }

        if (hasDoubleRed(root)) {
            return false;
        }

        int blackHeight = getBlackHeight(root);
        return blackHeight != -1;
    }

    private boolean hasDoubleRed(TreeNode node) {
        if (node == null) return false;

        if (isRed(node)) {
            if (isRed(node.left) || isRed(node.right)) {
                return true;
            }
        }

        return hasDoubleRed(node.left) || hasDoubleRed(node.right);
    }

    private int getBlackHeight(TreeNode node) {
        if (node == null) return 1;

        int leftHeight = getBlackHeight(node.left);
        int rightHeight = getBlackHeight(node.right);

        if (leftHeight != rightHeight) {
            return -1;
        }

        return leftHeight + (isBlack(node) ? 1 : 0);
    }
}

// Класс для отображения графиков с тремя линиями
class MultiSeriesGraphPanel extends JPanel {
    private final java.util.List<Integer> xValues;
    private final java.util.List<Integer> experimentalY;
    private final java.util.List<Double> upperTheoreticalY;
    private final java.util.List<Double> lowerTheoreticalY;
    private final String title;
    private final String xLabel;
    private final String yLabel;

    public MultiSeriesGraphPanel(java.util.List<Integer> xValues,
                                 java.util.List<Integer> experimentalY,
                                 java.util.List<Double> upperTheoreticalY,
                                 java.util.List<Double> lowerTheoreticalY,
                                 String title, String xLabel, String yLabel) {
        this.xValues = xValues;
        this.experimentalY = experimentalY;
        this.upperTheoreticalY = upperTheoreticalY;
        this.lowerTheoreticalY = lowerTheoreticalY;
        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 70;
        int labelPadding = 40;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        if (xValues == null || xValues.isEmpty() || experimentalY == null || experimentalY.isEmpty()) {
            g2d.setColor(Color.RED);
            g2d.drawString("Нет данных для отображения", width/2 - 80, height/2);
            return;
        }

        int minX = java.util.Collections.min(xValues);
        int maxX = java.util.Collections.max(xValues);
        int minY = 0;

        // Находим максимальное значение среди всех серий
        int maxExperimentalY = java.util.Collections.max(experimentalY);
        double maxUpperY = 0;
        double maxLowerY = 0;

        for (Double val : upperTheoreticalY) {
            if (val > maxUpperY) maxUpperY = val;
        }
        for (Double val : lowerTheoreticalY) {
            if (val > maxLowerY) maxLowerY = val;
        }

        int maxY = Math.max(maxExperimentalY, (int)Math.ceil(Math.max(maxUpperY, maxLowerY)));

        if (maxY == 0) maxY = 10;
        maxY = (int)(maxY * 1.1);

        double xScale = (double) (width - 2 * padding - labelPadding) / (maxX - minX);
        double yScale = (double) (height - 2 * padding - labelPadding) / (maxY - minY);

        // Рисуем сетку
        g2d.setColor(new Color(240, 240, 240));
        Stroke gridStroke = new BasicStroke(1);
        g2d.setStroke(gridStroke);

        for (int i = 0; i <= 10; i++) {
            int x = padding + labelPadding + (i * (width - 2 * padding - labelPadding) / 10);
            g2d.drawLine(x, height - padding - labelPadding, x, padding);

            int xValue = minX + (i * (maxX - minX) / 10);
            String xLabelStr = Integer.toString(xValue);
            FontMetrics metrics = g2d.getFontMetrics();
            int labelWidth = metrics.stringWidth(xLabelStr);
            g2d.setColor(Color.BLACK);
            g2d.drawString(xLabelStr, x - labelWidth / 2, height - padding + 20);
        }

        for (int i = 0; i <= 10; i++) {
            int y = height - padding - labelPadding - (i * (height - 2 * padding - labelPadding) / 10);
            g2d.setColor(new Color(240, 240, 240));
            g2d.drawLine(padding + labelPadding, y, width - padding, y);

            int yValue = minY + (i * (maxY - minY) / 10);
            String yLabelStr = Integer.toString(yValue);
            FontMetrics metrics = g2d.getFontMetrics();
            int labelWidth = metrics.stringWidth(yLabelStr);
            g2d.setColor(Color.BLACK);
            g2d.drawString(yLabelStr, padding + labelPadding - labelWidth - 10, y + 5);
        }

        // Рисуем оси
        g2d.setColor(Color.BLACK);
        Stroke axisStroke = new BasicStroke(2);
        g2d.setStroke(axisStroke);

        g2d.drawLine(padding + labelPadding, height - padding - labelPadding,
                width - padding, height - padding - labelPadding);
        g2d.drawLine(padding + labelPadding, height - padding - labelPadding,
                padding + labelPadding, padding);

        // Стрелки на осях
        drawArrow(g2d, width - padding, height - padding - labelPadding,
                width - padding - 10, height - padding - labelPadding - 5);
        drawArrow(g2d, width - padding, height - padding - labelPadding,
                width - padding - 10, height - padding - labelPadding + 5);
        drawArrow(g2d, padding + labelPadding, padding,
                padding + labelPadding - 5, padding + 10);
        drawArrow(g2d, padding + labelPadding, padding,
                padding + labelPadding + 5, padding + 10);

        // Подписи осей
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics metrics = g2d.getFontMetrics();
        int xLabelWidth = metrics.stringWidth(xLabel);
        g2d.drawString(xLabel, (width - xLabelWidth) / 2, height - padding / 3);

        Graphics2D g2dRotated = (Graphics2D) g2d.create();
        g2dRotated.rotate(-Math.PI / 2);
        g2dRotated.drawString(yLabel, -height / 2 - metrics.stringWidth(yLabel) / 2, padding / 2);
        g2dRotated.dispose();

        // Заголовок
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        metrics = g2d.getFontMetrics();
        int titleWidth = metrics.stringWidth(title);
        g2d.drawString(title, (width - titleWidth) / 2, padding / 2);

        // Рисуем экспериментальные данные (синяя сплошная линия)
        g2d.setColor(Color.BLUE);
        Stroke experimentalStroke = new BasicStroke(2.5f);
        g2d.setStroke(experimentalStroke);

        java.util.List<Point> experimentalPoints = new java.util.ArrayList<>();
        for (int i = 0; i < xValues.size(); i++) {
            int x = (int) (padding + labelPadding + (xValues.get(i) - minX) * xScale);
            int y = (int) (height - padding - labelPadding - (experimentalY.get(i) - minY) * yScale);
            experimentalPoints.add(new Point(x, y));
        }

        for (int i = 0; i < experimentalPoints.size() - 1; i++) {
            Point p1 = experimentalPoints.get(i);
            Point p2 = experimentalPoints.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // Рисуем точки на экспериментальной линии
        g2d.setColor(Color.BLUE);
        for (Point p : experimentalPoints) {
            g2d.fillOval(p.x - 4, p.y - 4, 8, 8);
        }

        // Рисуем верхнюю теоретическую оценку (красная пунктирная линия)
        if (!upperTheoreticalY.isEmpty()) {
            g2d.setColor(Color.RED);
            Stroke upperStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);
            g2d.setStroke(upperStroke);

            java.util.List<Point> upperPoints = new java.util.ArrayList<>();
            for (int i = 0; i < xValues.size(); i++) {
                int x = (int) (padding + labelPadding + (xValues.get(i) - minX) * xScale);
                int y = (int) (height - padding - labelPadding - (upperTheoreticalY.get(i) - minY) * yScale);
                upperPoints.add(new Point(x, y));
            }

            for (int i = 0; i < upperPoints.size() - 1; i++) {
                Point p1 = upperPoints.get(i);
                Point p2 = upperPoints.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Рисуем нижнюю теоретическую оценку (зеленая пунктирная линия)
        if (!lowerTheoreticalY.isEmpty()) {
            g2d.setColor(Color.GREEN);
            Stroke lowerStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);
            g2d.setStroke(lowerStroke);

            java.util.List<Point> lowerPoints = new java.util.ArrayList<>();
            for (int i = 0; i < xValues.size(); i++) {
                int x = (int) (padding + labelPadding + (xValues.get(i) - minX) * xScale);
                int y = (int) (height - padding - labelPadding - (lowerTheoreticalY.get(i) - minY) * yScale);
                lowerPoints.add(new Point(x, y));
            }

            for (int i = 0; i < lowerPoints.size() - 1; i++) {
                Point p1 = lowerPoints.get(i);
                Point p2 = lowerPoints.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Легенда
        int legendX = width - 200;
        int legendY = padding;

        // Экспериментальные данные
        g2d.setColor(Color.BLUE);
        g2d.fillRect(legendX, legendY, 20, 20);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(legendX, legendY, 20, 20);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Экспериментальная", legendX + 30, legendY + 15);

        // Верхняя оценка
        if (!upperTheoreticalY.isEmpty()) {
            g2d.setColor(Color.RED);
            g2d.fillRect(legendX, legendY + 30, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(legendX, legendY + 30, 20, 20);
            g2d.drawString("Верхняя оценка", legendX + 30, legendY + 45);
        }

        // Нижняя оценка
        if (!lowerTheoreticalY.isEmpty()) {
            g2d.setColor(Color.GREEN);
            g2d.fillRect(legendX, legendY + 60, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(legendX, legendY + 60, 20, 20);
            g2d.drawString("Нижняя оценка", legendX + 30, legendY + 75);
        }
    }

    private void drawArrow(Graphics2D g2d, int x, int y, int x1, int y1) {
        g2d.drawLine(x, y, x1, y1);
    }
}

// Класс для визуализации дерева
class TreeVisualizer extends JPanel {
    private TreeNode root;
    private final int nodeRadius = 20;
    private final int horizontalSpacing = 40;
    private final int verticalSpacing = 60;
    private static final int MAX_VISIBLE_NODES = 500;

    public TreeVisualizer(TreeNode root) {
        this.root = root;
        setPreferredSize(new Dimension(1200, 800));
        setBackground(Color.WHITE);
    }

    public void setRoot(TreeNode root) {
        this.root = root;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (root != null) {
            int nodeCount = countNodes(root);
            if (nodeCount > MAX_VISIBLE_NODES) {
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                g2d.setColor(Color.RED);
                String message = "Дерево слишком большое для визуализации (" + nodeCount + " узлов)";
                g2d.drawString(message, getWidth() / 2 - 200, getHeight() / 2);
                g2d.setFont(new Font("Arial", Font.PLAIN, 16));
                g2d.drawString("Максимум " + MAX_VISIBLE_NODES + " узлов для отображения",
                        getWidth() / 2 - 150, getHeight() / 2 + 30);
                return;
            }

            java.util.Map<TreeNode, Point> positions = new java.util.HashMap<>();
            calculatePositions(root, getWidth() / 2, 50, positions);
            drawTree(g2d, root, positions);
        } else {
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.setColor(Color.RED);
            g2d.drawString("Дерево пустое", getWidth() / 2 - 80, getHeight() / 2);
        }
    }

    private int countNodes(TreeNode node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    private void calculatePositions(TreeNode node, int x, int y, java.util.Map<TreeNode, Point> positions) {
        if (node == null) return;

        positions.put(node, new Point(x, y));

        int leftWidth = getSubtreeWidth(node.left);
        int rightWidth = getSubtreeWidth(node.right);

        if (node.left != null) {
            calculatePositions(node.left, x - (rightWidth + 1) * horizontalSpacing,
                    y + verticalSpacing, positions);
        }

        if (node.right != null) {
            calculatePositions(node.right, x + (leftWidth + 1) * horizontalSpacing,
                    y + verticalSpacing, positions);
        }
    }

    private int getSubtreeWidth(TreeNode node) {
        if (node == null) return 0;
        return 1 + getSubtreeWidth(node.left) + getSubtreeWidth(node.right);
    }

    private void drawTree(Graphics2D g2d, TreeNode node, java.util.Map<TreeNode, Point> positions) {
        if (node == null) return;

        Point currentPos = positions.get(node);

        // Рисуем связи
        if (node.left != null) {
            Point leftPos = positions.get(node.left);
            g2d.setColor(Color.BLACK);
            g2d.drawLine(currentPos.x, currentPos.y, leftPos.x, leftPos.y);
            drawTree(g2d, node.left, positions);
        }

        if (node.right != null) {
            Point rightPos = positions.get(node.right);
            g2d.setColor(Color.BLACK);
            g2d.drawLine(currentPos.x, currentPos.y, rightPos.x, rightPos.y);
            drawTree(g2d, node.right, positions);
        }

        // Рисуем узел
        Color nodeColor = Color.CYAN;
        Color textColor = Color.BLACK;

        if (node instanceof AVLTreeNode) {
            nodeColor = Color.GREEN;
            textColor = Color.BLACK;
        } else if (node instanceof RBTreeNode) {
            RBTreeNode rbNode = (RBTreeNode) node;
            if (rbNode.color == RBTreeNode.Color.RED) {
                nodeColor = Color.RED;
                textColor = Color.BLACK;
            } else {
                nodeColor = Color.BLACK;
                textColor = Color.WHITE;
            }
        }

        g2d.setColor(nodeColor);
        g2d.fillOval(currentPos.x - nodeRadius, currentPos.y - nodeRadius,
                2 * nodeRadius, 2 * nodeRadius);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(currentPos.x - nodeRadius, currentPos.y - nodeRadius,
                2 * nodeRadius, 2 * nodeRadius);

        // Рисуем значение
        g2d.setColor(textColor);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String text = node.toString();
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.drawString(text, currentPos.x - textWidth / 2, currentPos.y + textHeight / 4);

        // Для AVL деревьев показываем высоту
        if (node instanceof AVLTreeNode) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            String heightText = "h=" + ((AVLTreeNode) node).height;
            g2d.drawString(heightText, currentPos.x - 15, currentPos.y - nodeRadius - 5);
        }
    }
}

// Класс для интерактивного тестирования
class InteractiveTester extends JFrame {
    private BinarySearchTree currentTree;
    private final JTextArea outputArea;
    private final JTextField inputField;
    private final Random random;
    private final TreeVisualizer treeVisualizer;
    private final JLabel treeTypeLabel;
    private final JLabel statusLabel;
    private final JProgressBar progressBar;
    private int operationCount;

    public InteractiveTester() {
        setTitle("Расширенный интерактивный тестер деревьев v3.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        currentTree = new BinarySearchTree();
        random = new Random();
        operationCount = 0;

        JPanel controlPanel = new JPanel(new GridLayout(2, 5, 5, 5));

        JButton bstButton = new JButton("BST");
        JButton avlButton = new JButton("AVL");
        JButton rbButton = new JButton("Красно-черное");
        JButton insertButton = new JButton("Вставить");
        JButton deleteButton = new JButton("Удалить");
        JButton searchButton = new JButton("Найти");
        JButton randomButton = new JButton("10 случайных");
        JButton monotonicButton = new JButton("10 монотонных");
        JButton validateButton = new JButton("Проверить");
        JButton clearButton = new JButton("Очистить");
        JButton traverseButton = new JButton("Обходы");
        JButton random100Button = new JButton("100 случайных");
        JButton statsButton = new JButton("Статистика");
        JButton debugButton = new JButton("Отладка");

        controlPanel.add(bstButton);
        controlPanel.add(avlButton);
        controlPanel.add(rbButton);
        controlPanel.add(insertButton);
        controlPanel.add(deleteButton);
        controlPanel.add(searchButton);
        controlPanel.add(randomButton);
        controlPanel.add(monotonicButton);
        controlPanel.add(validateButton);
        controlPanel.add(clearButton);
        controlPanel.add(random100Button);
        controlPanel.add(statsButton);
        controlPanel.add(debugButton);

        JPanel extraPanel = new JPanel(new FlowLayout());
        extraPanel.add(traverseButton);

        treeTypeLabel = new JLabel("Текущее дерево: BST | Узлов: 0");
        treeTypeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        extraPanel.add(treeTypeLabel);

        statusLabel = new JLabel("Готов");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.BLUE);
        extraPanel.add(statusLabel);

        progressBar = new JProgressBar(0, 100);
        progressBar.setVisible(false);
        extraPanel.add(progressBar);

        treeVisualizer = new TreeVisualizer(currentTree.root);
        JScrollPane visualizerScroll = new JScrollPane(treeVisualizer);
        visualizerScroll.setPreferredSize(new Dimension(800, 400));

        outputArea = new JTextArea(10, 60);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane outputScroll = new JScrollPane(outputArea);

        inputField = new JTextField(20);
        inputField.addActionListener(e -> {
            if (!inputField.getText().trim().isEmpty()) {
                insertButton.doClick();
            } else {
                showStatus("Введите значение", Color.RED, 2000);
            }
        });

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Значение:"));
        inputPanel.add(inputField);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.NORTH);
        topPanel.add(extraPanel, BorderLayout.SOUTH);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                visualizerScroll, outputScroll);
        mainSplit.setDividerLocation(400);

        add(topPanel, BorderLayout.NORTH);
        add(mainSplit, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        bstButton.addActionListener(e -> {
            currentTree = new BinarySearchTree();
            treeVisualizer.setRoot(currentTree.root);
            updateTreeTypeLabel();
            outputArea.append("[" + (++operationCount) + "] Переключено на BST дерево\n");
            showStatus("BST дерево активировано", Color.GREEN, 2000);
        });

        avlButton.addActionListener(e -> {
            currentTree = new AVLTree();
            treeVisualizer.setRoot(currentTree.root);
            updateTreeTypeLabel();
            outputArea.append("[" + (++operationCount) + "] Переключено на AVL дерево\n");
            showStatus("AVL дерево активировано", Color.GREEN, 2000);
        });

        rbButton.addActionListener(e -> {
            currentTree = new RedBlackTree();
            treeVisualizer.setRoot(currentTree.root);
            updateTreeTypeLabel();
            outputArea.append("[" + (++operationCount) + "] Переключено на Красно-черное дерево\n");
            showStatus("Красно-черное дерево активировано", Color.GREEN, 2000);
        });

        insertButton.addActionListener(e -> {
            String input = inputField.getText().trim();
            if (input.isEmpty()) {
                showStatus("Введите значение для вставки", Color.RED, 2000);
                return;
            }

            try {
                int value = Integer.parseInt(input);
                TreeNode result = currentTree.insertValue(value, false);
                if (result != null) {
                    outputArea.append("[" + (++operationCount) + "] Вставлено: " + value + "\n");
                    treeVisualizer.setRoot(currentTree.root);
                    updateTreeDisplay();
                    showStatus("Элемент " + value + " успешно вставлен", Color.GREEN, 2000);
                } else {
                    outputArea.append("[" + (++operationCount) + "] Дубликат: " + value + "\n");
                    showStatus("Элемент " + value + " уже существует", Color.ORANGE, 2000);
                }
            } catch (NumberFormatException ex) {
                outputArea.append("[" + (++operationCount) + "] Ошибка: '" + input + "' не является числом\n");
                showStatus("Некорректный ввод. Введите целое число", Color.RED, 3000);
            }
        });

        deleteButton.addActionListener(e -> {
            String input = inputField.getText().trim();
            if (input.isEmpty()) {
                showStatus("Введите значение для удаления", Color.RED, 2000);
                return;
            }

            try {
                int value = Integer.parseInt(input);
                TreeNode result = currentTree.removeValue(value);
                if (result != null) {
                    outputArea.append("[" + (++operationCount) + "] Удалено: " + value + "\n");
                    treeVisualizer.setRoot(currentTree.root);
                    updateTreeDisplay();
                    showStatus("Элемент " + value + " успешно удален", Color.GREEN, 2000);
                } else {
                    outputArea.append("[" + (++operationCount) + "] Не найдено: " + value + "\n");
                    showStatus("Элемент " + value + " не найден", Color.ORANGE, 2000);
                }
            } catch (NumberFormatException ex) {
                outputArea.append("[" + (++operationCount) + "] Ошибка: '" + input + "' не является числом\n");
                showStatus("Некорректный ввод. Введите целое число", Color.RED, 3000);
            }
        });

        searchButton.addActionListener(e -> {
            String input = inputField.getText().trim();
            if (input.isEmpty()) {
                showStatus("Введите значение для поиска", Color.RED, 2000);
                return;
            }

            try {
                int value = Integer.parseInt(input);
                TreeNode result = currentTree.locateNode(value);
                if (result != null) {
                    outputArea.append("[" + (++operationCount) + "] Найдено: " + value +
                            " (родитель: " + (result.parent != null ? result.parent.value : "корень") + ")\n");
                    showStatus("Элемент " + value + " найден", Color.GREEN, 2000);
                } else {
                    outputArea.append("[" + (++operationCount) + "] Не найдено: " + value + "\n");
                    showStatus("Элемент " + value + " не найден", Color.ORANGE, 2000);
                }
            } catch (NumberFormatException ex) {
                outputArea.append("[" + (++operationCount) + "] Ошибка: '" + input + "' не является числом\n");
                showStatus("Некорректный ввод. Введите целое число", Color.RED, 3000);
            }
        });

        randomButton.addActionListener(e -> {
            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() {
                    progressBar.setVisible(true);
                    for (int i = 0; i < 10; i++) {
                        int value = random.nextInt(100);
                        currentTree.insertValue(value, false);
                        publish(i + 1);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    return null;
                }

                @Override
                protected void process(java.util.List<Integer> chunks) {
                    int last = chunks.get(chunks.size() - 1);
                    progressBar.setValue(last * 10);
                    showStatus("Добавлено " + last + " из 10 элементов", Color.BLUE, 0);
                }

                @Override
                protected void done() {
                    try {
                        get();
                        outputArea.append("[" + (++operationCount) + "] Добавлено 10 случайных чисел\n");
                        treeVisualizer.setRoot(currentTree.root);
                        updateTreeDisplay();
                        showStatus("10 случайных элементов добавлено", Color.GREEN, 2000);
                    } catch (Exception ex) {
                        outputArea.append("[" + (++operationCount) + "] Ошибка: " + ex.getMessage() + "\n");
                        showStatus("Ошибка при добавлении элементов", Color.RED, 3000);
                    } finally {
                        progressBar.setVisible(false);
                    }
                }
            };
            worker.execute();
        });

        monotonicButton.addActionListener(e -> {
            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() {
                    progressBar.setVisible(true);
                    int start = random.nextInt(100);
                    for (int i = 0; i < 10; i++) {
                        currentTree.insertValue(start + i, false);
                        publish(i + 1);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    return null;
                }

                @Override
                protected void process(java.util.List<Integer> chunks) {
                    int last = chunks.get(chunks.size() - 1);
                    progressBar.setValue(last * 10);
                    showStatus("Добавлено " + last + " из 10 элементов", Color.BLUE, 0);
                }

                @Override
                protected void done() {
                    try {
                        get();
                        outputArea.append("[" + (++operationCount) + "] Добавлено 10 монотонно возрастающих чисел\n");
                        treeVisualizer.setRoot(currentTree.root);
                        updateTreeDisplay();
                        showStatus("10 монотонных элементов добавлено", Color.GREEN, 2000);
                    } catch (Exception ex) {
                        outputArea.append("[" + (++operationCount) + "] Ошибка: " + ex.getMessage() + "\n");
                        showStatus("Ошибка при добавлении элементов", Color.RED, 3000);
                    } finally {
                        progressBar.setVisible(false);
                    }
                }
            };
            worker.execute();
        });

        random100Button.addActionListener(e -> {
            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() {
                    progressBar.setVisible(true);
                    for (int i = 0; i < 100; i++) {
                        int value = random.nextInt(1000);
                        currentTree.insertValue(value, false);
                        if (i % 10 == 0) {
                            publish(i + 1);
                        }
                    }
                    return null;
                }

                @Override
                protected void process(java.util.List<Integer> chunks) {
                    int last = chunks.get(chunks.size() - 1);
                    progressBar.setValue(last);
                    showStatus("Добавлено " + last + " из 100 элементов", Color.BLUE, 0);
                }

                @Override
                protected void done() {
                    try {
                        get();
                        outputArea.append("[" + (++operationCount) + "] Добавлено 100 случайных чисел\n");
                        treeVisualizer.setRoot(currentTree.root);
                        updateTreeDisplay();
                        showStatus("100 случайных элементов добавлено", Color.GREEN, 2000);
                    } catch (Exception ex) {
                        outputArea.append("[" + (++operationCount) + "] Ошибка: " + ex.getMessage() + "\n");
                        showStatus("Ошибка при добавлении элементов", Color.RED, 3000);
                    } finally {
                        progressBar.setVisible(false);
                    }
                }
            };
            worker.execute();
        });

        validateButton.addActionListener(e -> {
            String validationResult;
            Color statusColor;

            if (currentTree instanceof AVLTree) {
                boolean valid = ((AVLTree)currentTree).validateAVL();
                validationResult = "Проверка AVL: " + (valid ? "OK" : "Ошибка");
                statusColor = valid ? Color.GREEN : Color.RED;
            } else if (currentTree instanceof RedBlackTree) {
                boolean valid = ((RedBlackTree)currentTree).validateRB();
                validationResult = "Проверка RB: " + (valid ? "OK" : "Ошибка");
                statusColor = valid ? Color.GREEN : Color.RED;
            } else {
                boolean valid = currentTree.validateBST();
                validationResult = "Проверка BST: " + (valid ? "OK" : "Ошибка");
                statusColor = valid ? Color.GREEN : Color.RED;
            }

            outputArea.append("[" + (++operationCount) + "] " + validationResult + "\n");
            showStatus(validationResult, statusColor, 3000);
        });

        clearButton.addActionListener(e -> {
            currentTree.clear();
            treeVisualizer.setRoot(currentTree.root);
            outputArea.append("[" + (++operationCount) + "] Дерево очищено\n");
            updateTreeTypeLabel();
            showStatus("Дерево очищено", Color.GREEN, 2000);
        });

        traverseButton.addActionListener(e -> {
            outputArea.append("[" + (++operationCount) + "] === Обходы дерева ===\n");
            outputArea.append("Прямой обход: " + currentTree.preOrderTraversal(currentTree.root) + "\n");
            outputArea.append("Центрированный обход: " + currentTree.inOrderTraversal(currentTree.root) + "\n");
            outputArea.append("Обратный обход: " + currentTree.postOrderTraversal(currentTree.root) + "\n");
            outputArea.append("Обход в ширину: " + currentTree.levelOrderTraversal() + "\n");
            showStatus("Обходы дерева выполнены", Color.GREEN, 2000);
        });

        statsButton.addActionListener(e -> {
            outputArea.append("[" + (++operationCount) + "] === Статистика дерева ===\n");
            outputArea.append("Тип дерева: " + currentTree.getClass().getSimpleName() + "\n");
            outputArea.append("Количество узлов: " + (currentTree.getNodeCount()) + "\n");
            outputArea.append("Высота: " + currentTree.computeHeight(null) + "\n");
            outputArea.append("Минимум: " +
                    (currentTree.findSmallest() != null ? currentTree.findSmallest().value : "нет") + "\n");
            outputArea.append("Максимум: " +
                    (currentTree.findLargest() != null ? currentTree.findLargest().value : "нет") + "\n");
            outputArea.append("Количество операций: " + operationCount + "\n");
            showStatus("Статистика выведена", Color.GREEN, 2000);
        });

        debugButton.addActionListener(e -> {
            outputArea.append("[" + (++operationCount) + "] === Отладка дерева ===\n");
            if (currentTree.root != null) {
                outputArea.append("Корень: " + currentTree.root.value + "\n");
                outputArea.append("Левый потомок корня: " +
                        (currentTree.root.left != null ? currentTree.root.left.value : "нет") + "\n");
                outputArea.append("Правый потомок корня: " +
                        (currentTree.root.right != null ? currentTree.root.right.value : "нет") + "\n");
                outputArea.append("Структура дерева:\n" + currentTree.toString() + "\n");
            } else {
                outputArea.append("Дерево пустое\n");
            }
            showStatus("Отладочная информация выведена", Color.BLUE, 2000);
        });

        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    private void updateTreeDisplay() {
        updateTreeTypeLabel();
    }

    private void updateTreeTypeLabel() {
        String type = "BST";
        if (currentTree instanceof AVLTree) type = "AVL";
        else if (currentTree instanceof RedBlackTree) type = "Красно-черное";

        treeTypeLabel.setText("Текущее дерево: " + type + " | Узлов: " + currentTree.getNodeCount());
    }

    private void showStatus(String message, Color color, int duration) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);

        if (duration > 0) {
            Timer timer = new Timer(duration, e -> {
                statusLabel.setText("Готов");
                statusLabel.setForeground(Color.BLUE);
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            InteractiveTester tester = new InteractiveTester();
            tester.setVisible(true);
        });

        System.out.println("=== Автоматическое тестирование ===");
        testBST();
        testAVL();
        testRedBlack();
        testMonotonicKeys();
        buildAVLGraphs();
        buildRBGraphs();
    }

    private static void testBST() {
        System.out.println("\n--- Тестирование BST ---");
        BinarySearchTree bst = new BinarySearchTree();
        Random random = new Random();

        // Тестирование вставки и поиска
        System.out.println("Тестирование вставки...");
        for (int i = 0; i < 10; i++) {
            int value = random.nextInt(100);
            bst.insertValue(value);
            TreeNode found = bst.locateNode(value);
            if (found == null) {
                System.out.println("ОШИБКА: элемент " + value + " не найден после вставки!");
            } else {
                System.out.println("OK: элемент " + value + " найден");
            }
        }

        System.out.println("BST высота: " + bst.computeHeight(null));
        System.out.println("BST валидно: " + bst.validateBST());
        System.out.println("Количество узлов: " + bst.getNodeCount());

        // Тестирование удаления корня
        if (bst.root != null) {
            int rootValue = bst.root.value;
            System.out.println("Удаляем корень: " + rootValue);
            bst.removeValue(rootValue);
            TreeNode found = bst.locateNode(rootValue);
            if (found != null) {
                System.out.println("ОШИБКА: корень " + rootValue + " не удален!");
            } else {
                System.out.println("OK: корень успешно удален");
            }
        }
    }

    private static void testAVL() {
        System.out.println("\n--- Тестирование AVL ---");
        AVLTree avl = new AVLTree();

        // Тестирование балансировки
        System.out.println("Тестирование балансировки AVL...");
        for (int i = 0; i < 20; i++) {
            avl.insertValue(i * 10);
        }

        System.out.println("AVL высота: " + avl.computeHeight(null));
        System.out.println("AVL валидно: " + avl.validateAVL());

        // Тестирование поиска
        System.out.println("Поиск элемента 50: " + (avl.locateNode(50) != null ? "найден" : "не найден"));
        System.out.println("Поиск элемента 999: " + (avl.locateNode(999) != null ? "найден" : "не найден"));
    }

    private static void testRedBlack() {
        System.out.println("\n--- Тестирование красно-черного дерева ---");
        RedBlackTree rb = new RedBlackTree();
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            rb.insertValue(random.nextInt(100));
        }

        System.out.println("RB высота: " + rb.computeHeight(null));
        System.out.println("RB валидно: " + rb.validateRB());

        if (rb.validateRB()) {
            System.out.println("Все свойства RB дерева выполнены!");
        }
    }

    private static void testMonotonicKeys() {
        System.out.println("\n--- Тестирование монотонно возрастающих ключей ---");

        BinarySearchTree bst = new BinarySearchTree();
        AVLTree avl = new AVLTree();
        RedBlackTree rb = new RedBlackTree();

        int n = 100;
        System.out.println("Вставка " + n + " монотонно возрастающих ключей:");

        for (int i = 0; i < n; i++) {
            bst.insertValue(i);
            avl.insertValue(i);
            rb.insertValue(i);
        }

        double log2n = Math.log(n) / Math.log(2);
        int expectedLogHeight = (int) Math.ceil(log2n);

        System.out.println("BST высота (монотонные): " + bst.computeHeight(null) +
                " (ожидается ~" + n + ")");
        System.out.println("AVL высота (монотонные): " + avl.computeHeight(null) +
                " (ожидается ~" + expectedLogHeight + ")");
        System.out.println("RB высота (монотонные): " + rb.computeHeight(null) +
                " (ожидается ~" + (2 * expectedLogHeight) + ")");
    }

    // Окно с графиками для AVL дерева
    private static void buildAVLGraphs() {
        System.out.println("\n=== Построение графиков для AVL дерева ===");

        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame avlFrame = new JFrame("Графики высоты AVL дерева");
            avlFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            avlFrame.setLayout(new GridLayout(1, 2));

            int maxN = 20000;
            int step = 100;

            // Данные для случайных ключей
            java.util.List<Integer> randomX = new java.util.ArrayList<>();
            java.util.List<Integer> randomExperimentalY = new java.util.ArrayList<>();
            java.util.List<Double> randomUpperY = new java.util.ArrayList<>();
            java.util.List<Double> randomLowerY = new java.util.ArrayList<>();

            AVLTree avlRandom = new AVLTree();

            // Создаем и перемешиваем массив случайных чисел
            java.util.List<Integer> randomKeys = new java.util.ArrayList<>();
            for (int i = 0; i < maxN; i++) {
                randomKeys.add(i);
            }
            java.util.Collections.shuffle(randomKeys);

            for (int n = 0; n <= maxN; n += step) {
                randomX.add(n);

                if (n == 0) {
                    randomExperimentalY.add(0);
                } else {
                    // Вставляем следующие step элементов
                    for (int i = n - step; i < n; i++) {
                        avlRandom.insertValue(randomKeys.get(i));
                    }
                    int height = avlRandom.computeHeight(null);
                    randomExperimentalY.add(height);
                }

                // Теоретические оценки для AVL
                double upperBound = 1.44 * (Math.log(n + 2) / Math.log(2)) - 0.328;
                double lowerBound = Math.log(n + 1) / Math.log(2);

                randomUpperY.add(upperBound);
                randomLowerY.add(lowerBound);
            }

            // Данные для монотонных ключей
            java.util.List<Integer> monotonicX = new java.util.ArrayList<>();
            java.util.List<Integer> monotonicExperimentalY = new java.util.ArrayList<>();
            java.util.List<Double> monotonicUpperY = new java.util.ArrayList<>();
            java.util.List<Double> monotonicLowerY = new java.util.ArrayList<>();

            AVLTree avlMonotonic = new AVLTree();

            for (int n = 0; n <= maxN; n += step) {
                monotonicX.add(n);

                if (n == 0) {
                    monotonicExperimentalY.add(0);
                } else {
                    // Вставляем следующие step элементов
                    for (int i = n - step; i < n; i++) {
                        avlMonotonic.insertValue(i);
                    }
                    int height = avlMonotonic.computeHeight(null);
                    monotonicExperimentalY.add(height);
                }

                // Теоретические оценки для AVL
                double upperBound = 1.44 * (Math.log(n + 2) / Math.log(2)) - 0.328;
                double lowerBound = Math.log(n + 1) / Math.log(2);

                monotonicUpperY.add(upperBound);
                monotonicLowerY.add(lowerBound);
            }

            // График для случайных ключей
            MultiSeriesGraphPanel randomPanel = new MultiSeriesGraphPanel(
                    randomX, randomExperimentalY, randomUpperY, randomLowerY,
                    "AVL дерево - случайные ключи (n до " + maxN + ")",
                    "Количество элементов (n)",
                    "Высота дерева"
            );

            // График для монотонных ключей
            MultiSeriesGraphPanel monotonicPanel = new MultiSeriesGraphPanel(
                    monotonicX, monotonicExperimentalY, monotonicUpperY, monotonicLowerY,
                    "AVL дерево - монотонные ключи (n до " + maxN + ")",
                    "Количество элементов (n)",
                    "Высота дерева"
            );

            avlFrame.add(randomPanel);
            avlFrame.add(monotonicPanel);

            avlFrame.setSize(1600, 600);
            avlFrame.setLocationRelativeTo(null);
            avlFrame.setVisible(true);
        });

        System.out.println("Графики для AVL дерева построены.");
    }

    // Окно с графиками для красно-черного дерева
    private static void buildRBGraphs() {
        System.out.println("\n=== Построение графиков для красно-черного дерева ===");

        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                JFrame rbFrame = new JFrame("Графики высоты красно-черного дерева");
                rbFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                rbFrame.setLayout(new GridLayout(1, 2));

                int maxN = 20000;
                int step = 100;

                // Данные для случайных ключей
                java.util.List<Integer> randomX = new java.util.ArrayList<>();
                java.util.List<Integer> randomExperimentalY = new java.util.ArrayList<>();
                java.util.List<Double> randomUpperY = new java.util.ArrayList<>();
                java.util.List<Double> randomLowerY = new java.util.ArrayList<>();

                RedBlackTree rbRandom = new RedBlackTree();

                // Создаем и перемешиваем массив случайных чисел
                java.util.List<Integer> randomKeys = new java.util.ArrayList<>();
                for (int i = 0; i < maxN; i++) {
                    randomKeys.add(i);
                }
                java.util.Collections.shuffle(randomKeys);

                for (int n = 0; n <= maxN; n += step) {
                    randomX.add(n);

                    if (n == 0) {
                        randomExperimentalY.add(0);
                    } else {
                        // Вставляем следующие step элементов
                        for (int i = n - step; i < n; i++) {
                            rbRandom.insertValue(randomKeys.get(i));
                        }
                        int height = rbRandom.computeHeight(null);
                        randomExperimentalY.add(height);
                    }

                    // Теоретические оценки для RB дерева (как в Python)
                    double lowerBound = (n == 0) ? 0 : Math.log(n + 1) / Math.log(2);
                    double upperBound = (n == 0) ? 0 : 2 * (Math.log(n + 1) / Math.log(2));

                    randomUpperY.add(upperBound);
                    randomLowerY.add(lowerBound);
                }

                // Данные для монотонных ключи
                java.util.List<Integer> monotonicX = new java.util.ArrayList<>();
                java.util.List<Integer> monotonicExperimentalY = new java.util.ArrayList<>();
                java.util.List<Double> monotonicUpperY = new java.util.ArrayList<>();
                java.util.List<Double> monotonicLowerY = new java.util.ArrayList<>();

                RedBlackTree rbMonotonic = new RedBlackTree();

                for (int n = 0; n <= maxN; n += step) {
                    monotonicX.add(n);

                    if (n == 0) {
                        monotonicExperimentalY.add(0);
                    } else {
                        // Вставляем следующие step элементов
                        for (int i = n - step; i < n; i++) {
                            rbMonotonic.insertValue(i);
                        }
                        int height = rbMonotonic.computeHeight(null);
                        monotonicExperimentalY.add(height);
                    }

                    // Теоретические оценки для RB дерева (как в Python)
                    double lowerBound = (n == 0) ? 0 : Math.log(n + 1) / Math.log(2);
                    double upperBound = (n == 0) ? 0 : 2 * (Math.log(n + 1) / Math.log(2));

                    monotonicUpperY.add(upperBound);
                    monotonicLowerY.add(lowerBound);
                }

                // График для случайных ключей
                MultiSeriesGraphPanel randomPanel = new MultiSeriesGraphPanel(
                        randomX, randomExperimentalY, randomUpperY, randomLowerY,
                        "Красно-черное дерево - случайные ключи (n до " + maxN + ")",
                        "Количество элементов (n)",
                        "Высота дерева"
                );

                // График для монотонных ключей
                MultiSeriesGraphPanel monotonicPanel = new MultiSeriesGraphPanel(
                        monotonicX, monotonicExperimentalY, monotonicUpperY, monotonicLowerY,
                        "Красно-черное дерево - монотонные ключи (n до " + maxN + ")",
                        "Количество элементов (n)",
                        "Высота дерева"
                );

                rbFrame.add(randomPanel);
                rbFrame.add(monotonicPanel);

                rbFrame.setSize(1600, 600);
                rbFrame.setLocationRelativeTo(null);
                rbFrame.setVisible(true);

            } catch (Exception e) {
                System.err.println("Ошибка при создании графиков RB дерева: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Ошибка при создании графиков RB дерева: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        System.out.println("Графики для красно-черного дерева построены.");
        System.out.println("\n=== Тестирование завершено успешно ===");
    }
}