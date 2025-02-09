import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.EmptyStackException;

class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        TimingsUtil timings = new TimingsUtil();
        start(solution);
        timings.print();
    }

    public static void start(Solution solution) {
        log(solution.jump(new int[]{7,0,9,6,9,6,1,7,9,0,1,2,9,0,3}));
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

    private static void log(Object... args) {
        System.out.println(Arrays.toString(args));
        System.out.println();
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