import java.awt.*;
import java.util.*;
import javax.swing.*;

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
            if (node.left == null && node.right == null) {
                return null;
            } else if (node.left == null) {
                node.right.parent = node.parent;
                return node.right;
            } else if (node.right == null) {
                node.left.parent = node.parent;
                return node.left;
            } else {
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

        if (balance > 1 && getBalanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        if (balance > 1 && getBalanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && getBalanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

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

    private RBTreeNode.Color getColor(TreeNode node) {
        return node == null ? BLACK : ((RBTreeNode) node).color;
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
        TreeNode deletedNode = removeRecursive(root, value);
        if (deletedNode != null) {
            nodeCount--;
            if (root != null) {
                setColor(root, BLACK);
            }
        }
        return deletedNode;
    }

    private TreeNode removeRecursive(TreeNode node, int value) {
        if (node == null) return null;

        TreeNode parent = null;
        TreeNode current = node;
        TreeNode deletedNode = null;
        RBTreeNode.Color deletedColor = BLACK;

        while (current != null) {
            if (value == current.value) {
                deletedNode = current;
                deletedColor = getColor(current);

                if (current.left == null) {
                    TreeNode child = current.right;
                    if (parent == null) {
                        root = child;
                    } else if (current == parent.left) {
                        parent.left = child;
                    } else {
                        parent.right = child;
                    }

                    if (child != null) {
                        child.parent = parent;
                    }

                    current = child;
                } else if (current.right == null) {
                    TreeNode child = current.left;
                    if (parent == null) {
                        root = child;
                    } else if (current == parent.left) {
                        parent.left = child;
                    } else {
                        parent.right = child;
                    }

                    if (child != null) {
                        child.parent = parent;
                    }

                    current = child;
                } else {
                    TreeNode successor = findMinNode(current.right);
                    current.value = successor.value;
                    value = successor.value;
                    parent = current;
                    current = current.right;
                }
            } else if (value < current.value) {
                parent = current;
                current = current.left;
            } else {
                parent = current;
                current = current.right;
            }
        }

        if (deletedColor == BLACK && root != null) {
            if (current == null) {
                // Узел был листом
                if (parent != null) {
                    fixDeletion(null, parent);
                }
            } else {
                fixDeletion(current, parent != null ? parent : current.parent);
            }
        }

        return deletedNode;
    }

    private void fixDeletion(TreeNode node, TreeNode parent) {
        TreeNode current = node;

        while (current != root && isBlack(current)) {
            if (current == parent.left) {
                TreeNode sibling = parent.right;

                if (isRed(sibling)) {
                    setColor(sibling, BLACK);
                    setColor(parent, RED);
                    rotateLeft(parent);
                    sibling = parent.right;
                }

                if (isBlack(sibling.left) && isBlack(sibling.right)) {
                    setColor(sibling, RED);
                    current = parent;
                    parent = current.parent;
                } else {
                    if (isBlack(sibling.right)) {
                        setColor(sibling.left, BLACK);
                        setColor(sibling, RED);
                        rotateRight(sibling);
                        sibling = parent.right;
                    }

                    setColor(sibling, getColor(parent));
                    setColor(parent, BLACK);
                    setColor(sibling.right, BLACK);
                    rotateLeft(parent);
                    current = root;
                }
            } else {
                TreeNode sibling = parent.left;

                if (isRed(sibling)) {
                    setColor(sibling, BLACK);
                    setColor(parent, RED);
                    rotateRight(parent);
                    sibling = parent.left;
                }

                if (isBlack(sibling.left) && isBlack(sibling.right)) {
                    setColor(sibling, RED);
                    current = parent;
                    parent = current.parent;
                } else {
                    if (isBlack(sibling.left)) {
                        setColor(sibling.right, BLACK);
                        setColor(sibling, RED);
                        rotateLeft(sibling);
                        sibling = parent.left;
                    }

                    setColor(sibling, getColor(parent));
                    setColor(parent, BLACK);
                    setColor(sibling.left, BLACK);
                    rotateRight(parent);
                    current = root;
                }
            }
        }

        setColor(current, BLACK);
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

        g2d.setColor(Color.BLACK);
        Stroke axisStroke = new BasicStroke(2);
        g2d.setStroke(axisStroke);

        g2d.drawLine(padding + labelPadding, height - padding - labelPadding,
                width - padding, height - padding - labelPadding);
        g2d.drawLine(padding + labelPadding, height - padding - labelPadding,
                padding + labelPadding, padding);

        drawArrow(g2d, width - padding, height - padding - labelPadding,
                width - padding - 10, height - padding - labelPadding - 5);
        drawArrow(g2d, width - padding, height - padding - labelPadding,
                width - padding - 10, height - padding - labelPadding + 5);
        drawArrow(g2d, padding + labelPadding, padding,
                padding + labelPadding - 5, padding + 10);
        drawArrow(g2d, padding + labelPadding, padding,
                padding + labelPadding + 5, padding + 10);

        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics metrics = g2d.getFontMetrics();
        int xLabelWidth = metrics.stringWidth(xLabel);
        g2d.drawString(xLabel, (width - xLabelWidth) / 2, height - padding / 3);

        Graphics2D g2dRotated = (Graphics2D) g2d.create();
        g2dRotated.rotate(-Math.PI / 2);
        g2dRotated.drawString(yLabel, -height / 2 - metrics.stringWidth(yLabel) / 2, padding / 2);
        g2dRotated.dispose();

        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        metrics = g2d.getFontMetrics();
        int titleWidth = metrics.stringWidth(title);
        g2d.drawString(title, (width - titleWidth) / 2, padding / 2);

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

        g2d.setColor(Color.BLUE);
        for (Point p : experimentalPoints) {
            g2d.fillOval(p.x - 4, p.y - 4, 8, 8);
        }

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

        int legendX = width - 200;
        int legendY = padding;

        g2d.setColor(Color.BLUE);
        g2d.fillRect(legendX, legendY, 20, 20);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(legendX, legendY, 20, 20);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Экспериментальная", legendX + 30, legendY + 15);

        if (!upperTheoreticalY.isEmpty()) {
            g2d.setColor(Color.RED);
            g2d.fillRect(legendX, legendY + 30, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(legendX, legendY + 30, 20, 20);
            g2d.drawString("Верхняя оценка", legendX + 30, legendY + 45);
        }

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

public class Main {
    public static void main(String[] args) {

        System.out.println("=== Автоматическое тестирование ===");
        testBST();
        testAVL();
        testRedBlack();
        testMonotonicKeys();
        buildAVLGraphs();
        buildRBGraphs();
        buildBSTGraphs();
    }

    private static void testBST() {
        System.out.println("\n--- Тестирование BST ---");
        BinarySearchTree bst = new BinarySearchTree();
        Random random = new Random();

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

        System.out.println("Тестирование балансировки AVL...");
        for (int i = 0; i < 20; i++) {
            avl.insertValue(i * 10);
        }

        System.out.println("AVL высота: " + avl.computeHeight(null));
        System.out.println("AVL валидно: " + avl.validateAVL());

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

        System.out.println("AVL высота (монотонные): " + avl.computeHeight(null) +
                " (ожидается ~" + expectedLogHeight + ")");
        System.out.println("RB высота (монотонные): " + rb.computeHeight(null) +
                " (ожидается ~" + (2 * expectedLogHeight) + ")");
    }

    private static void buildAVLGraphs() {
        System.out.println("\n=== Построение графиков для AVL дерева ===");

        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame avlFrame = new JFrame("Графики высоты AVL дерева");
            avlFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            avlFrame.setLayout(new GridLayout(1, 2));

            int maxN = 20000;
            int step = 100;

            java.util.List<Integer> randomX = new java.util.ArrayList<>();
            java.util.List<Integer> randomExperimentalY = new java.util.ArrayList<>();
            java.util.List<Double> randomUpperY = new java.util.ArrayList<>();
            java.util.List<Double> randomLowerY = new java.util.ArrayList<>();

            AVLTree avlRandom = new AVLTree();

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
                    for (int i = n - step; i < n; i++) {
                        avlRandom.insertValue(randomKeys.get(i));
                    }
                    int height = avlRandom.computeHeight(null);
                    randomExperimentalY.add(height);
                }

                double upperBound = 1.44 * (Math.log(n + 2) / Math.log(2)) - 0.328;
                double lowerBound = Math.log(n + 1) / Math.log(2);

                randomUpperY.add(upperBound);
                randomLowerY.add(lowerBound);
            }

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
                    for (int i = n - step; i < n; i++) {
                        avlMonotonic.insertValue(i);
                    }
                    int height = avlMonotonic.computeHeight(null);
                    monotonicExperimentalY.add(height);
                }

                double upperBound = 1.44 * (Math.log(n + 2) / Math.log(2)) - 0.328;
                double lowerBound = Math.log(n + 1) / Math.log(2);

                monotonicUpperY.add(upperBound);
                monotonicLowerY.add(lowerBound);
            }

            MultiSeriesGraphPanel randomPanel = new MultiSeriesGraphPanel(
                    randomX, randomExperimentalY, randomUpperY, randomLowerY,
                    "AVL дерево - случайные ключи (n до " + maxN + ")",
                    "Количество элементов (n)",
                    "Высота дерева"
            );

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

    private static void buildRBGraphs() {
        System.out.println("\n=== Построение графиков для красно-черного дерева ===");

        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                JFrame rbFrame = new JFrame("Графики высоты красно-черного дерева");
                rbFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                rbFrame.setLayout(new GridLayout(1, 2));

                int maxN = 20000;
                int step = 100;

                java.util.List<Integer> randomX = new java.util.ArrayList<>();
                java.util.List<Integer> randomExperimentalY = new java.util.ArrayList<>();
                java.util.List<Double> randomUpperY = new java.util.ArrayList<>();
                java.util.List<Double> randomLowerY = new java.util.ArrayList<>();

                RedBlackTree rbRandom = new RedBlackTree();

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
                        for (int i = n - step; i < n; i++) {
                            rbRandom.insertValue(randomKeys.get(i));
                        }
                        int height = rbRandom.computeHeight(null);
                        randomExperimentalY.add(height);
                    }

                    double lowerBound = (n == 0) ? 0 : Math.log(n + 1) / Math.log(2);
                    double upperBound = (n == 0) ? 0 : 2 * (Math.log(n + 1) / Math.log(2));

                    randomUpperY.add(upperBound);
                    randomLowerY.add(lowerBound);
                }

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
                        for (int i = n - step; i < n; i++) {
                            rbMonotonic.insertValue(i);
                        }
                        int height = rbMonotonic.computeHeight(null);
                        monotonicExperimentalY.add(height);
                    }

                    double lowerBound = (n == 0) ? 0 : Math.log(n + 1) / Math.log(2);
                    double upperBound = (n == 0) ? 0 : 2 * (Math.log(n + 1) / Math.log(2));

                    monotonicUpperY.add(upperBound);
                    monotonicLowerY.add(lowerBound);
                }

                MultiSeriesGraphPanel randomPanel = new MultiSeriesGraphPanel(
                        randomX, randomExperimentalY, randomUpperY, randomLowerY,
                        "Красно-черное дерево - случайные ключи (n до " + maxN + ")",
                        "Количество элементов (n)",
                        "Высота дерева"
                );

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
    }

    private static void buildBSTGraphs() {
        System.out.println("\n=== Построение графиков для BST (бинарное дерево поиска) ===");

        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                JFrame bstFrame = new JFrame("Графики высоты BST");
                bstFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                bstFrame.setLayout(new GridLayout(1, 2));

                int maxN = 20000;
                int step = 100;

                java.util.List<Integer> randomX = new java.util.ArrayList<>();
                java.util.List<Integer> randomExperimentalY = new java.util.ArrayList<>();
                java.util.List<Double> randomUpperY = new java.util.ArrayList<>();
                java.util.List<Double> randomLowerY = new java.util.ArrayList<>();

                BinarySearchTree bstRandom = new BinarySearchTree();

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
                        for (int i = n - step; i < n; i++) {
                            bstRandom.insertValue(randomKeys.get(i));
                        }
                        int height = bstRandom.computeHeight(null);
                        randomExperimentalY.add(height);
                    }
                }

                MultiSeriesGraphPanel randomPanel = new MultiSeriesGraphPanel(
                        randomX, randomExperimentalY, randomUpperY, randomLowerY,
                        "BST - случайные ключи (n до " + maxN + ")",
                        "Количество элементов (n)",
                        "Высота дерева"
                );

                bstFrame.add(randomPanel);

                bstFrame.setSize(1600, 600);
                bstFrame.setLocationRelativeTo(null);
                bstFrame.setVisible(true);
                
            } catch (Exception e) {
                System.err.println("Ошибка при создании графика BST: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Ошибка при создании графика BST: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        System.out.println("Графики для BST построены.");
        System.out.println("\n=== Тестирование завершено успешно ===");
    }
}