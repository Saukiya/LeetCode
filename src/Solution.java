import java.util.*;

class Solution {

    private static void log(Object... args) {
        System.out.println(Arrays.toString(args));
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        TimingsUtil timings = new TimingsUtil();
        start(solution);
        timings.print();
    }

    public static void start(Solution solution) {
        log(" === " + solution.catMouseGame(new int[][]{{2,6},{2,4,5,6},{0,1,3,5,6},{2},{1,5,6},{1,2,4},{0,1,2,4}}), 2);
        log(" === " + solution.catMouseGame(new int[][]{{3,4},{3,5},{3,6},{0,1,2},{0,5,6},{1,4},{2,4}}), 0);
        log(" === " + solution.catMouseGame(new int[][]{{2,3,4},{2,4},{0,1,4},{0,4},{0,1,2,3}}), 2);
        log(" === " + solution.catMouseGame(new int[][]{{2,3},{2},{0,1},{0,4},{3}}), 2);
        log(" === " + solution.catMouseGame(new int[][]{{2,3},{3,4},{0,4},{0,1},{1,2}}), 1);
        log(" === " + solution.catMouseGame(new int[][]{{2,5},{3},{0,4,5},{1,4,5},{2,3},{0,2,3}}), 0);
        log(" === " + solution.catMouseGame(new int[][]{{1,3},{0},{3},{0,2}}), 1);
    }

    // https://leetcode.cn/problems/cat-and-mouse/description/?envType=daily-question&envId=2025-02-10
    public int catMouseGame(int[][] graph) {
        // 老鼠1点触发, 终点是0点 TODO 如果有多条最短路线
        int[] shuPath = catMouseGame_shu(graph, 1, 0, new NumberCombiner(2), 0, 0);
        log("shu", Arrays.toString(shuPath));
        if (shuPath == null) return 2;
        // 猫2点出发, 寻路的总步数不能大于老鼠
        int[] maoPath = catMouseGame_mao(graph, 2, shuPath, new NumberCombiner(0), 0, shuPath.length);
        log("mao", Arrays.toString(maoPath));
        if (maoPath != null) {
            
            // TODO 猫能抓到老鼠, 判断老鼠是否能逃生, 如果没有则返回2
            // TODO 老鼠的所有临边节点的下一个节点所需步数 == 猫到达此节点的步数时
            
            // TODO 判断是否有其他节点可以走
            
            shuPath = catMouseGame_shu(graph, shuPath[maoPath.length - 2], shuPath[Math.min(maoPath.length, shuPath.length - 1)], new NumberCombiner(maoPath[maoPath.length - 1]), 0, 0);
            log("shu", Arrays.toString(shuPath));
            if (shuPath == null) return 2;
        } else return 1;
        return 0;
    }

    /**
     * 寻路逻辑
     * 返回路径int[]
     * 
     * @param graph 地图节点
     * @param current 当前位置
     * @param target 目标位置
     * @param black 不能去的地方
     * @param length 移动长度
     * @return
     */
    public int[] catMouseGame_shu(int[][] graph, int current, int target, NumberCombiner black, int length, int max) {
        length++;
        black.add(current);
        if (length == max) return null; // 超出最小步数直接取消
        int min = max;
        int[] result = null;
        for (int i : graph[current]) {
            if (i == target) {
                result = new int[length + 1];
                result[length] = i;
                break;
            }
            if (black.contains(i)) continue;
            int[] temp = catMouseGame_shu(graph, i, target, black, length, min);
            if (temp == null) {
                black.remove(i);
                continue;
            }
            min = temp.length;
            (result = temp)[length] = i;
        }
        if (result != null && length == 1) {
            result[0] = current;
        }
        return result;
    }

    /**
     * 寻路逻辑
     * 返回路径int[]
     *
     * @param graph 地图节点
     * @param current 当前位置
     * @param shuPath 老鼠沿途路径
     * @param black 不能去的地方
     * @param length 移动长度
     * @return
     */
    public int[] catMouseGame_mao(int[][] graph, int current, int[] shuPath, NumberCombiner black, int length, int max) {
        length++;
        black.add(current);
        if (length == max) return null; // 超出最小步数直接取消
        int min = max;
        int[] result = null;
        do1:
        for (int i : graph[current]) {
            if (black.contains(i)) continue;
            for (int si = 0; si < shuPath.length; si++) {
                if (i == shuPath[si] && si + 1 > length) {
                    result = new int[length + 1];
                    result[length] = i;
                    break do1;
                }
            }
            int[] temp = catMouseGame_mao(graph, i, shuPath, black, length, min);
            if (temp == null) {
                black.remove(i);
                continue;
            }
            // 如果比之前的路径更短
            min = temp.length;
            (result = temp)[length] = i;
        }
        if (result != null && length == 1) {
            result[0] = current;
        }
        return result;
    }

    public int jump(int[] nums) {
        // left-用来查询上个step的终点, right-用来查询当前step的终点
        // 尽可能延长当前step的最大值, right = max(当前位置+步数, right)
        // 如果 i == 上一个step终点(left), 那么 step+1, left = right
        int left = 0, right = 0, step = 0;
        for (int i = 0, length = nums.length - 1; i < length; i++) {
            right = Math.max(i + nums[i], right);
            if (left == i) {
                left = right;
                step++;
            }
        }
        return step;
    }

    public boolean isMatch(String s, String p) {
        char[] sc = s.toCharArray();
        char[] pc = p.toCharArray();
        // 默认双指针, star双指针
        int si = 0, pi = 0, starSi = -1, starPi = 0, pl = pc.length, sl = sc.length;
        // 根据模式字符串做遍历
        while (pi < pl) {
            char c = pc[pi];
            if (c == '*') { // 通配符找到star时, 重置star记录
                // 找到连续的最后一个star -> "*****"
                while (pi < pl && pc[pi] == '*') pi++;
                // 如果star是最后一个通配符, 直接返回true
                if (pi == pl) return true;
                // 记录star起始位置
                starSi = si;
                starPi = pi;
            } else if (si < sl && (c == '?' || c == sc[si]) && (pi + 1 != pl || si + 1 == sl)) {
                // 除了star以外的匹配成功时, 并且没有哪一边字符耗完的情况下 (pi + 1 != pl || si + 1 == sl)
                si++;
                pi++;
            } else if (starSi != -1 && ++starSi < sl) {
                // 匹配失败但是star模式时, starSi++并重置默认双指针
                si = starSi;
                pi = starPi;
            } else return false;
        }
        return si == sl && pi == pl;
    }

    // AI优化
//    public String multiply(String num1, String num2) {
//        char[] c1 = num1.toCharArray();
//        char[] c2 = num2.toCharArray();
//        int[] result = new int[c1.length + c2.length]; // 预分配结果数组
//
//        // 逐位相乘并累加
//        for (int i = c1.length - 1; i >= 0; i--) {
//            int n1 = c1[i] - '0';
//            for (int j = c2.length - 1; j >= 0; j--) {
//                int n2 = c2[j] - '0';
//                int sum = result[i + j + 1] + n1 * n2;
//                result[i + j + 1] = sum % 10;
//                result[i + j] += sum / 10; // 自动处理进位
//            }
//        }
//
//        // 转换为字符串
//        StringBuilder sb = new StringBuilder();
//        for (int num : result) {
//            if (!(sb.length() == 0 && num == 0)) { // 跳过前导零
//                sb.append(num);
//            }
//        }
//        return sb.length() == 0 ? "0" : sb.toString();
//    }

    public String multiply(String num1, String num2) {
        if ("0".equals(num1) || "0".equals(num2)) return "0";
        String result = "";
        char[] c1 = num1.toCharArray(), c2 = num2.toCharArray();
        for (int i = 0, c1l = c1.length; i < c1l; i++) {
            result = multiply_x(c2, c1[c1l - i - 1] - '0', i, result);
        }
        return result;
    }

    public String multiply_x(char[] c1, int multiply, int bits, String num2) {
        StringBuilder result = new StringBuilder();
        char[] c2 = num2.toCharArray();
        int carry = 0;
        for (int i = 0, c1l = c1.length, c2l = c2.length, length = Math.max(c1l + bits, c2l); i < length; i++) {
            int a1 = i >= bits && (c1l - i + bits) > 0 ? c1[c1l - i - 1 + bits] - '0' : 0;
            int a2 = (c2l - i) > 0 ? c2[c2l - i - 1] - '0' : 0;
            int temp = a1 * multiply + carry + a2;
            result.insert(0, (temp % 10));
            carry = temp / 10;
        }
        if (carry > 0) {
            result.insert(0, carry);
        }
        return result.toString();
    }

    public int trap(int[] heights) {
        int result = 0;
        int nextHeight = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < heights.length; i++) {
            int height = heights[i];
            if (height == 0) continue;
            while (!stack.isEmpty()) {
                int peek = stack.peek();
                int width = i - peek - 1;
                int addHeight = Math.min(heights[peek], height) - nextHeight;
                nextHeight += addHeight;
                result += width * addHeight;
                // 如果左侧小于等于右侧 删除左侧元素
                if (heights[peek] > height) break;
                stack.pop();
            }
            nextHeight = 0;
            stack.push(i);
        }
        return result;
    }

    public int firstMissingPositive(int[] nums) {
        int length = nums.length;
        for (int i = 0; i < nums.length; i++) {
            int temp = nums[i];
            while (temp > 0 && temp <= length && nums[i] != nums[nums[i] - 1]) {
                nums[i] = nums[temp - 1];
                nums[temp - 1] = temp;
                temp = nums[i];
            }
        }
        int result = 0;
        do {
            result++;
        } while (result <= length && result == nums[result - 1]);
        return result;
    }

    public int removeDuplicates(int[] nums) {
        int length = nums.length;
        int left = 2, right = 2;

        while (right < length) {
            if (nums[right] != nums[left - 2]) {
                nums[left] = nums[right];
                left++;
            }
            right++;
        }
        return left;
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        // 对数组进行排序，确保相同元素相邻
        Arrays.sort(nums);
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, new ArrayList<>(), res);
        return res;
    }

    private void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> res) {
        // 当路径长度等于数组长度时，说明已经生成了一个完整的排列
        if (path.size() == nums.length) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            // 如果当前元素已经被使用，跳过
            if (used[i]) {
                continue;
            }
            // 避免生成重复排列
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }
            // 标记当前元素为已使用
            used[i] = true;
            path.add(nums[i]);
            // 递归生成下一个位置的元素
            backtrack(nums, used, path, res);
            // 回溯操作，撤销选择
            used[i] = false;
            path.remove(path.size() - 1);
        }
    }

    public static class IntStack {

        int[] stack;
        int top = -1;

        public IntStack() {
            this(8);
        }

        public IntStack(int size) {
            stack = new int[size];
        }

        public void push(int value) {
            if (++top == stack.length) resize();
            stack[top] = value;
        }

        public int pop() {
            if (top == -1) throw new EmptyStackException();
            return stack[top--];
        }

        public int peek() {
            if (top == -1) throw new EmptyStackException();
            return stack[top];
        }

        public int size() {
            return top + 1;
        }

        public boolean empty() {
            return size() == 0;
        }

        private void resize() {
            int[] newStack = new int[stack.length * 2];
            System.arraycopy(stack, 0, newStack, 0, stack.length);
            stack = newStack;
        }
    }
}