package com.leetcode.helper.model.listnode;

import com.leetcode.helper.model.util.JsonUtils;
import com.leetcode.helper.model.util.HelperException;
import com.leetcode.helper.model.HelperNode;

import java.util.Iterator;
import java.util.List;

public class ListNode implements HelperNode {

    public int val;

    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    @Override
    public HelperNode convert(String parameter) throws Exception {
        if (parameter.contains(".")) {
            throw new HelperException("parameter may have decimal, not support yet");
        }
        List<Integer> list = JsonUtils.parseArray(parameter, Integer.class);
        if (list.size() == 0) {
            return null;
        }
        Iterator<Integer> iterator = list.iterator();
        ListNode listNode = this;
        while (iterator.hasNext()) {
            listNode.val = iterator.next();
            if (iterator.hasNext()) {
                listNode.next = new ListNode();
                listNode = listNode.next;
            }
        }
        return this;
    }

    @Override
    public HelperNode[] convertArray(String parameter) {
        return null;
    }

    @Override
    public String toString() {
        ListNode node = this;
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        while (node != null) {
            sb.append(node.val);
            sb.append(',');
            node = node.next;
        }
        sb.setLength(sb.length() - 1);
        sb.append(']');
        return sb.toString();
    }
}
