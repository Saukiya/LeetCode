import java.util.*;

class Solution {

    static TimingsUtil timings;

    private static void log(Object... args) {
        System.out.println(Arrays.toString(args));
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        timings = new TimingsUtil();
        start(solution);
        timings.print();
    }

    public static void start(Solution solution) {
        solution.rotateRight(head(), 1);
        solution.rotateRight(head(), 2);
        solution.rotateRight(head(), 3);
        solution.rotateRight(head(), 4);
        solution.rotateRight(head(), 5);
        solution.rotateRight(head(), 6);
        solution.rotateRight(head(), 7);
    }

    public static ListNode head() {
        ListNode head = new ListNode(1);
        ListNode tail = head;
        for (int i = 2; i <= 5; i++) {
            tail = (tail.next = new ListNode(i));
        }
        return head;
    }

    public int uniquePaths(int m, int n) {
        int temp = 1;
        return 0;
    }

    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode last = head, tail = head;
        int count = 1;
        while (tail.next != null) {
            count++;
            tail = tail.next;
        }
        k = count - k % count;
        while (--k > 0) {
            last = last.next;
        }
        tail.next = head;
        head = last.next;
        last.next = null;
        return head;
    }

    // 直接获取集合n的第k个排列
    public String getPermutation(int n, int k) {
        // 算出所有阶乘
        int[] factorials = new int[n];
        int temp = 1;
        for (int i = 1; i < n; i++) {
            temp = factorials[i] = temp * i;
        }

        k--;
        StringBuilder sb = new StringBuilder();
        boolean[] block = new boolean[n];
        for (int i = n; i > 1; i--) {
            // 获取当前可能存在的子排列
            int value = factorials[i - 1];
            // 获得当前是第几个子排列
            int index = k / value;
            // 通过子排列索引获取数字
            int num = getPermutation_2(block, index);
            k %= value;
            sb.append(num);
        }
        // 最后一个数直接拿
        sb.append(getPermutation_2(block, 0));
        return sb.toString();
    }

    // 获取可用的数 如果需要1， 但是1被使用的话， 往后找没被用的数字 例如: ([f,t,t,f],1) -> 4
    public int getPermutation_2(boolean[] blocked, int n) {
        for (int i = 0; i < blocked.length; i++) {
            if (blocked[i]) continue;
            if (n-- <= 0) {
                blocked[i] = true;
                return i + 1;
            }
        }
        return 0;
    }

    public int[][] generateMatrix(int N) {
        int[][] result = new int[N][N];
        for (int i = 0; i < N; i++) {
            result[0][i] = i + 1;
        }
        // 每次步进后, M/N减1
        int m = 0, n = N - 1; // 起点为(0,N-1)
        int step = 1; // 步进
        int num = N + 1;
        // 转圈圈~
        while (--N > 0) {
            for (int i = 0; i < N; i++) {
                m += step;
                result[m][n] = num++;
            }
            step *= -1;
            for (int i = 0; i < N; i++) {
                n += step;
                result[m][n] = num++;
            }
        }
        return result;
    }

    public int lengthOfLastWord(String s) {
        int temp = -1;
        for (int length = s.length() - 1; length >= 0; length--) {
            if (s.charAt(length) != ' ') {
                if (temp == -1) {
                    temp = length;
                }
            } else if (temp != -1) {
                return temp - length;
            }
        }
        return temp + 1;
    }

    public int[][] insert(int[][] intervals, int[] newInterval) {
        int n = intervals.length;
        if (n == 0) {
            return new int[][]{newInterval};
        }
        // 区间外
        if (intervals[n - 1][1] < newInterval[0]) {
            int[][] result = new int[n + 1][];
            result[n] = newInterval;
            System.arraycopy(intervals, 0, result, 0, n);
            return result;
        }

        // 判断在区间内
        for (int i = 0; i < n; i++) {
            // 
            if (intervals[i][1] >= newInterval[0]) {
                // 插入
                if (intervals[i][0] > newInterval[1]) {
                    int[][] result = new int[n + 1][];
                    System.arraycopy(intervals, 0, result, 0, i);
                    result[i] = newInterval;
                    System.arraycopy(intervals, i, result, i + 1, n - i);
                    return result;
                }
                intervals[i][0] = Math.min(newInterval[0], intervals[i][0]);
                // 覆盖
                int j = i;
                while (++j < n) {
                    if (intervals[j][0] > newInterval[1]) break;
                }
                intervals[i][1] = Math.max(intervals[j - 1][1], newInterval[1]);
                int[][] result = new int[n - j + i + 1][];
                System.arraycopy(intervals, 0, result, 0, i + 1);
                System.arraycopy(intervals, j, result, i + 1, n - j);
                return result;
            }
        }
        return null;
    }

    public int maxDistance(List<List<Integer>> arrays) {
        int min1 = 10000, min2 = min1, max1 = -10000, max2 = max1;
        boolean repeated = false;
        for (List<Integer> array : arrays) {
            int left = array.get(0);
            int right = array.get(array.size() - 1);
            if (min1 > left) {
                min2 = min1;
                min1 = left;
                if (max1 < right) {
                    max2 = max1;
                    max1 = right;
                    repeated = true;
                } else {
                    repeated = false;
                }
            } else if (max1 <= right) {
                max2 = max1;
                max1 = right;
                repeated = false;
            } else {
                min2 = Math.min(min2, left);
                max2 = Math.max(max2, right);
            }
        }
        return !repeated ? max1 - min1 : Math.max(max1 - min2, max2 - min1);
    }

    // 填涂
    public int[][] merge(int[][] intervals) {
        boolean[] range = new boolean[10001];
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int[] ints : intervals) {
            Arrays.fill(range, ints[0], ints[1], true);
            min = Math.min(ints[0], min);
            max = Math.max(ints[1], max);
        }
        List<int[]> list = new ArrayList<>();
        int r = -1;
        for (int i = min; i <= max; i++) {
            if (r != -1) {
                if (!range[i]) {
                    list.add(new int[]{r, i});
                    r = -1;
                }
            } else if (range[i]) {
                r = i;
            }
        }
        if (r != -1) {
            list.add(new int[]{r, range.length});
        }
        return list.toArray(new int[list.size()][]);
    }

    // 排序+贪婪
    public int[][] merge_2(int[][] intervals) {
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o1[0], o2[0]);
            }
        });
        boolean[] blocked = new boolean[intervals.length];
        int count = intervals.length;
        for (int x = 0, L = intervals.length - 1; x < L; x++) {
            if (blocked[x]) continue;
            int[] X = intervals[x];
            for (int y = x + 1; y <= L; y++) {
                int[] Y = intervals[y];
                if (X[1] >= Y[0]) {
                    blocked[y] = true;
                    count--;
                    X[1] = Math.max(X[1], Y[1]);
                } else break;
            }
        }
        int[][] result = new int[count][];
        for (int i = blocked.length - 1; i >= 0; i--) {
            if (blocked[i]) continue;
            result[--count] = intervals[i];
        }
        return result;
    }

    // TreeMap+栈
    public int[][] merge_1(int[][] intervals) {
        Map<Integer, Integer> map = new TreeMap<>();
        for (int[] interval : intervals) {
            map.put(interval[0], map.getOrDefault(interval[0], 0) + 1);
            map.put(interval[1], map.getOrDefault(interval[1], 0) - 1);
        }

        List<int[]> result = new ArrayList<>();
        int left = 0, sum = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            log(entry.getKey() + ": " + entry.getValue());
            if (sum == 0) {
                if (entry.getValue() == 0) {
                    result.add(new int[]{entry.getKey(), entry.getKey()});
                    continue;
                }
                left = entry.getKey();
                sum = entry.getValue();
                continue;
            }
            sum += entry.getValue();
            if (sum == 0) {
                result.add(new int[]{left, entry.getKey()});
            }
        }
        return result.toArray(new int[result.size()][]);
    }

    public boolean canJump(int[] nums) {
        int max = 0;
        for (int i = 0, l = nums.length - 1; i < l; i++) {
            max = Math.max(nums[i], max);
            max--;
            if (max < 0) return false;
        }
        return true;
    }

    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        // 每次步进后, M/N减1
        int M = matrix.length - 1; // M行
        int N = matrix[0].length; // N列
        int m = 0, n = -1; // 起点为(0,-1)
        int step = 1; // 步进
        // 转圈圈~
        while (true) {
            for (int i = 0; i < N; i++) {
                n += step;
                result.add(matrix[m][n]);
            }
            if (N-- == 0) break;
            for (int i = 0; i < M; i++) {
                m += step;
                result.add(matrix[m][n]);
            }
            if (M-- == 0) break;
            // 步进完两条边后 * -1
            step *= -1;
        }
        return result;
    }

    public int maxSubArray(int[] nums) {
        int max = nums[0], sum = 0;
        for (int num : nums) {
            sum += num;
            if (max < sum) {
                max = sum;
            }
            if (sum < 0) {
                sum = 0;
            }
        }
        return max;
    }

    public int findSpecialInteger(int[] arr) {
        if (arr.length < 4) return arr[0];
        int n = arr.length;
        int z = n / 8;
        for (int i = z; i < n; i++) {
            if (arr[i] == arr[i - z]) {
                return arr[i];
            }
        }
        return -1;
    }

    int solveNQueens_N, solveNQueens_N1, solveNQueens_N2;
    String[] solveNQueens_S;

    public int totalNQueens(int n) {
        solveNQueens_N = n;
        solveNQueens_N1 = n * 2 - 1; // 偏移 X - Y
        solveNQueens_N2 = n * 3 - 2; // 偏移 X + Y

        return totalNQueens_1(0, 0L, false);
    }

    // 方法一: 位运算存储 只支持(n = 1-13)
    public int totalNQueens_1(int X, long value, boolean d) {
        if (X >= solveNQueens_N) {
            return d ? 2 : 1;
        }
        // X = 0 时只遍历一半 其他镜像输出
        int L = X == 0 ? solveNQueens_N - solveNQueens_N / 2 : solveNQueens_N;
        int sum = 0;
        for (int Y = 0; Y < L; Y++) {
            if ((value & (1L << Y)) != 0) continue;
            int Z1 = X - Y + solveNQueens_N1;
            if ((value & (1L << Z1)) != 0) continue;
            int Z2 = X + Y + solveNQueens_N2;
            if ((value & (1L << Z2)) != 0) continue;
            sum += totalNQueens_1(X + 1, value | (1L << Y) | (1L << Z1) | (1L << Z2), d || (X == 0 && Y < solveNQueens_N / 2));
        }
        return sum;
    }

    // https://leetcode.cn/problems/n-queens/submissions/599407733/?envType=daily-question&envId=2025-02-14
    public List<List<String>> solveNQueens(int n) {
        solveNQueens_N = n;
        solveNQueens_N1 = n * 2 - 1; // 偏移 X - Y
        solveNQueens_N2 = n * 3 - 2; // 偏移 X + Y
        solveNQueens_S = new String[n]; // 提前存储需要生成的字符串
        char[] chars = new char[n];
        Arrays.fill(chars, '.');
        for (int i = 0; i < n; i++) {
            chars[i] = 'Q';
            solveNQueens_S[i] = String.valueOf(chars);
            chars[i] = '.';
        }

        List<List<String>> result = new ArrayList<>();
        solveNQueens_1(result, new int[n], 0, 0L);
//        solveNQueens_2(result, new int[n], 0, new boolean[n * 5 - 2]);
        return result;
    }

    // 方法一: 位运算存储 只支持(n = 1-13)
    public void solveNQueens_1(List<List<String>> list, int[] content, int X, long value) {
        if (X >= solveNQueens_N) {
            solveNQueens_0(list, content, solveNQueens_N);
            return;
        }
        // X = 0 时只遍历一半 其他镜像输出
        for (int Y = 0, L = (X == 0 ? solveNQueens_N - solveNQueens_N / 2 : solveNQueens_N); Y < L; Y++) {
            if ((value & (1L << Y)) != 0) continue;
            int Z1 = X - Y + solveNQueens_N1;
            if ((value & (1L << Z1)) != 0) continue;
            int Z2 = X + Y + solveNQueens_N2;
            if ((value & (1L << Z2)) != 0) continue;
            content[X] = Y;
            solveNQueens_1(list, content, X + 1, value | (1L << Y) | (1L << Z1) | (1L << Z2));
        }
    }

    // 方法二: 数组存储
    public void solveNQueens_2(List<List<String>> list, int[] content, int X, boolean[] b) {
        if (X >= solveNQueens_N) {
            solveNQueens_0(list, content, solveNQueens_N);
            return;
        }
        // X = 0 时只遍历一半 其他镜像输出
        for (int Y = 0, L = (X == 0 ? solveNQueens_N - solveNQueens_N / 2 : solveNQueens_N); Y < L; Y++) {
            if (b[Y]) continue;
            int Z1 = X - Y + solveNQueens_N1;
            if (b[Z1]) continue;
            int Z2 = X + Y + solveNQueens_N2;
            if (b[Z2]) continue;
            b[Y] = b[Z1] = b[Z2] = true;
            content[X] = Y;
            solveNQueens_2(list, content, X + 1, b);
            b[Y] = b[Z1] = b[Z2] = false;
        }
    }

    // 输出字符串 content[0] 小于n的一半则镜像输出
    public void solveNQueens_0(List<List<String>> list, int[] content, int n) {
        content = content.clone();
        list.add(new EasyList(content));
        // 达成镜像条件
        if (content[0] < n / 2) {
            list.add(new MirrorList(content));
        }
    }

    class EasyList extends AbstractList<String> {

        int[] content;

        public EasyList(int[] content) {
            this.content = content;
        }

        @Override
        public String get(int index) {
            return solveNQueens_S[content[index]];
        }

        @Override
        public int size() {
            return solveNQueens_N;
        }
    }

    class MirrorList extends AbstractList<String> {

        int[] content;

        public MirrorList(int[] content) {
            this.content = content;
        }

        @Override
        public String get(int index) {
            return solveNQueens_S[solveNQueens_N - content[index] - 1];
        }

        @Override
        public int size() {
            return solveNQueens_N;
        }
    }

    public double myPow(double x, int n) {
        if (n == 0) return 1;
        if (n / 2 != 0) {
            double result = myPow(x, n / 2);
            return result * result * myPow(x, n % 2);
        } else if (n < 0) {
            return 1 / x;
        }
        return x;
    }

    // 最小的磁力最大？
    public int maxDistance(int[] position, int m) {
        Arrays.sort(position);
        // 最大值和最小值的平均值
        int left = 1, right = (position[position.length - 1] - position[0]) / (m - 1), ans = 0;
        d1:
        while (left <= right) {
            int mid = (left + right) / 2;
            int count = 1, last = position[0];
            for (int j = 1, length = position.length; j < length; j++) {
                int i = position[j];
                if (i - last >= mid) {
                    if (++count == m) {
                        ans = mid;
                        left = mid + 1;
                        continue d1;
                    }
                    last = i;
                }
            }
            right = mid - 1;
        }
        return ans;
    }

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<Integer, List<String>> map = new HashMap<>();
        for (String str : strs) {
            int[] counts = new int[26];
            for (int i = 0; i < str.length(); i++) {
                counts[str.charAt(i) - 97]++;
            }
            // Arrays.hashCode投机取巧 实际上还挺容易重复的, 应该用StringBuilder
//            StringBuilder builder = new StringBuilder();
//            for (int i = 0; i < 26; i++) {
//                if (counts[i] != 0) {
//                    builder.append((char) ('a' + i));
//                    builder.append(counts[i]);
//                }
//            }
            map.computeIfAbsent(Arrays.hashCode(counts), x -> new ArrayList<>()).add(str);
        }
        return new ArrayList<>(map.values());
    }

    public void rotate(int[][] matrix) {
        int N = matrix.length - 1, X = matrix.length / 2, Y = matrix.length - X, temp;
        for (int x = 0; x < X; x++) {
            for (int y = 0; y < Y; y++) {
                temp = matrix[x][y];
                matrix[x][y] = matrix[N - y][x];
                matrix[N - y][x] = matrix[N - x][N - y];
                matrix[N - x][N - y] = matrix[y][N - x];
                matrix[y][N - x] = temp;
            }
        }
        log(Arrays.deepToString(matrix));
    }

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> list = new LinkedList<>();
        permute_1(nums, list, new LinkedList<>(), new boolean[nums.length], 0);
        return list;
    }

    public void permute_1(int[] nums, List<List<Integer>> allList, LinkedList<Integer> list, boolean[] used, int length) {
        length++;
        for (int i = 0; i < used.length; i++) {
            if (used[i]) continue;
            used[i] = true;
            list.addLast(nums[i]);
            if (length < nums.length) {
                permute_1(nums, allList, list, used, length);
            } else {
                allList.add(new LinkedList<>(list));
            }
            list.removeLast();
            used[i] = false;
        }
    }

    public int countBalls(int lowLimit, int highLimit) {
        int max = 0;
        int[] ints = new int[45];
        for (int i = lowLimit; i <= highLimit; i++) {
            int sum = -1, x = i;
            while (x != 0) {
                sum += x % 10;
                x /= 10;
            }
            max = Math.max(++ints[sum], max);
        }
        return max;
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
     * @param graph   地图节点
     * @param current 当前位置
     * @param target  目标位置
     * @param black   不能去的地方
     * @param length  移动长度
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
     * @param graph   地图节点
     * @param current 当前位置
     * @param shuPath 老鼠沿途路径
     * @param black   不能去的地方
     * @param length  移动长度
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