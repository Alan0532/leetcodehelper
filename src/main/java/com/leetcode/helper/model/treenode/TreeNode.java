package com.leetcode.helper.model.treenode;

import com.leetcode.helper.model.util.JsonUtils;
import com.leetcode.helper.model.util.HelperException;
import com.leetcode.helper.model.HelperNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreeNode implements HelperNode {


    public int val;

    public TreeNode left;

    public TreeNode right;

    public TreeNode() {
    }

    public TreeNode(int x) {
        val = x;
    }

    @Override
    public HelperNode convert(String parameter) throws Exception {
        if (parameter.contains(".")) {
            throw new HelperException("parameter may have decimal, not support yet");
        }
        List<String> list = JsonUtils.parseArray(parameter, String.class);
        if (list.size() == 0) {
            return null;
        }

        //构造根节点
        TreeNode node = this;
        node.val = Integer.parseInt(list.remove(0));

        Queue<TreeNode> queue = new LinkedList<>();
        boolean poll = false;

        for (String val : list) {
            if (val != null) {
                TreeNode newNode = new TreeNode(Integer.parseInt(val));
                if (poll) {
                    node.right = newNode;
                } else {
                    node.left = newNode;
                }
                queue.offer(newNode);
            }
            if (poll) {
                node = queue.poll();
            }
            poll = !poll;
        }

        return this;
    }

    @Override
    public HelperNode[] convertArray(String parameter) {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        TreeNode node = this;

        Queue<TreeNode> queue = new LinkedList<>();

        queue.offer(node);

        int nullSize = 0;

        while (queue.size() != 0 && queue.size() != nullSize) {
            node = queue.poll();
            if (node != null) {
                sb.append(node.val).append(",");
                queue.offer(node.left);
                queue.offer(node.right);
                if (node.left == null) {
                    nullSize++;
                }
                if (node.right == null) {
                    nullSize++;
                }
            } else {
                sb.append("null,");
                nullSize--;
            }
        }

        sb.setLength(sb.length() - 1);
        sb.append(']');
        return sb.toString();
    }

}