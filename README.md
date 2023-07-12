本工具用于简化使用LeetCode Editor插件时构造参数的操作

1. 安装idea的LeetCode Editor插件


2. 打开插件的设置界面,Custom Template打勾


3. TempFilePath 选择到项目的com路径  如：D:\leetcodehelper\src\main\java\com


4. Code FileName 输入

```
$!velocityTool.camelCaseName(${question.titleSlug})
```

5. Code Template 输入

```
package com.leetcode.editor.cn;

import com.leetcode.helper.LeetCodeHelper;
import com.leetcode.helper.model.listnode.ListNode;
import com.leetcode.helper.model.treenode.TreeNode;

import java.util.*;

//${question.title}
public class $!velocityTool.camelCaseName(${question.titleSlug}) {

    public static void main(String[] args) {
        LeetCodeHelper.code("输入");
    }

${question.code}
}
```

6. 使用LeetCode Editor插件生成一道题目，以第一题两数之和为例

   将LeetCodeHelper.code(”输入“);

   改为LeetCodeHelper.code("nums = [2,7,11,15], target = 9");

   直接运行或者以Debug模式运行即可