package ss.pku.zyf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import binary_tree.TreeNode;
import linked_list.ListNode;

public class Answers {
	//just for test
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] array = { 1, 10, 2, 12 };
		System.out.println(calculateMax(array));

	}

	// *原因：
	// 1.布局层级过于复杂；
	// 2.View 在绘制过程中在 onDraw 方法内操作过多；
	// 3.内存泄漏导致内存不足；
	// 4.在主线程中执行过多耗时操作；
	// 5.滑动组件（如ListView）中内容加载耗时过多导致卡顿；
	// 6.大图片加载耗时过多导致白屏；
	// 7.线程过多。
	//
	// *优化方案：
	// 1.简化布局层级。优先使用性能较好的 ViewGroup ，比如 RelativeLayout ；多使用 <include> 和 <merge>
	// 标签来重用布局，减少层级；使用 ViewStub 来按需加载布局。
	// 2.优化绘制过程。不要在 onDraw 方法中创建新的局部对象，不要做耗时任务，尽量减少操作复杂度。
	// 3.注意内存泄漏。例如在使用静态变量或单例模式时，注意对象生命周期的长度问题。
	// 4.将耗时操作放到线程中去异步执行，避免主线程耗时过多导致ANR。
	// 5.优化 ListView ，如使用 ViewHolder ，滑动速度太快时不加载内容，尝试开启硬件加速等。
	// 6.优化 Bitmap 加载，可以根据实际需求对图片进行采样，压缩图片。
	// 7.采用线程池，重用内部线程，合理地控制线程数。
	/**
	 * 用户经常会在多终端上操作购物，现在用户的手机APP停留在购物记录列表界面，在电脑上购买了商品。请结合自己熟悉的移动操作系统(Android/
	 * iOS任选其一)，给出用户不离开购物记录界面，不操作下拉刷新之类的动作，能够实时展示用户最新的购物记录的解决方案(请综合考虑电量、
	 * 流量消耗给出最优方案)
	 */

	/**
	 * 背景: 查看包裹的物流详情时，我们希望看见按照时间逆序排序的物流节点信息(可以简单理解为: 操作时间 +
	 * 操作内容)，但是由于快递公司工作人员的误操作，对同一个包裹操作了多次(比如要派送时快递员对同一个包裹扫码多次，产生多条派送中的文本内容，
	 * 但是操作时间是不一样的)，也可能由于网络问题，快递公司回传给阿里巴巴的物流详情信息没有严格按照操作时间的先后顺序(比如，操作上是先揽收包裹，
	 * 再运输包裹，快递公司回传信息给阿里巴巴时，先回传了运输包裹的信息，再回传揽收包裹的信息)，
	 * 因此客户端拿到的物流详情可能是一个乱序又带有重复操作内容的物流节点列表。 问题:
	 * 请基于自己熟悉的平台技术(android/ios),实现一个物流详情排序算法(请使用归并排序算法，即把待排序序列分为若干个子序列，
	 * 每个子序列是有序的，然后再把有序子序列合并为整体有序序列)，要求如下: 过滤重复文本内容的物流节点，仅保留操作时间最早的那条。
	 * 按照时间逆序进行排序，请使用归并排序算法，禁止使用递归
	 */

	class OpNode {
		int opTime; // 操作时间
		String opContent; // 操作内容
	}

	// 过滤重复内容的节点
	public void filter(List<OpNode> list) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < list.size(); i++) {
			OpNode node = list.get(i);
			if (map.containsKey(node.opContent)) {
				if (map.get(node.opContent) > node.opTime) {
					// 如果时间更早，则删除较晚的节点
					list.remove(map.get(node.opContent));
					map.put(node.opContent, i);
				} else {
					// 否则删除该节点
					list.remove(i);
				}
			} else {
				map.put(node.opContent, i);
			}
		}
	}

	public void opSort(List<OpNode> list) {
		int size = 1, low, mid, high;
		int n = list.size();
		while (size <= n - 1) {
			low = 0;
			while (low + size <= n - 1) {
				mid = low + size - 1;
				high = mid + size;
				if (high > n - 1)
					high = n - 1;
				Merge(list, low, mid, high);// 调用归并子函数
				low = high + 1;
			}
			size *= 2;
		}
	}

	void Merge(List<OpNode> list, int low, int mid, int high) {// 将两个有序区归并为一个有序区
		int i = low, j = mid + 1, k = 0;
		List<OpNode> temp = new ArrayList<OpNode>(high - low + 1);
		while (i <= mid && j <= high) {
			if (list.get(i).opTime <= list.get(j).opTime)
				temp.add(k++, list.get(i++));
			else
				temp.add(k++, list.get(j++));
		}
		while (i <= mid)
			temp.add(k++, list.get(i++));
		while (j <= high)
			temp.add(k++, list.get(j++));
		for (i = low, k = 0; i <= high; i++, k++)
			list.add(i, temp.get(k));
	}

	/**
	 * 爬台阶问题，总共多少种爬法
	 * 
	 * @param n
	 */
	static int upStairs(int n) {
		if (n == 1) {
			return 1;
		}
		if (n == 2) {
			return 2;
		}
		return upStairs(n - 1) + upStairs(n - 2);
	}

	/**
	 * 搜狗面试题：给出一个 n*n 的矩阵，每个矩阵点用(0，1)来表示是否可达，给出和始、终点坐标，如何找到一条路径。
	 */
	static int[][] mark; // 标记位，防止循环走

	static boolean findPath(int[][] m, int n, int sx, int sy, int ex, int ey) {
		if (sx == ex && sy == ey) {
			printNode(sx, sy);
			return true;
		}
		if (0 <= sx - 1 && mark[sx - 1][sy] == 0 && m[sx - 1][sy] == 1) {
			mark[sx - 1][sy] = 1;
			if (findPath(m, n, sx - 1, sy, ex, ey)) {
				printNode(sx, sy);
				return true;
			} else {
				mark[sx - 1][sy] = 0;
			}
		}
		if (0 <= sy - 1 && mark[sx][sy - 1] == 0 && m[sx][sy - 1] == 1) {
			mark[sx][sy - 1] = 1;
			if (findPath(m, n, sx, sy - 1, ex, ey)) {
				printNode(sx, sy);
				return true;
			} else {
				mark[sx][sy - 1] = 0;
			}
		}
		if (sx + 1 < n && mark[sx + 1][sy] == 0 && m[sx + 1][sy] == 1) {
			mark[sx + 1][sy] = 1;
			if (findPath(m, n, sx + 1, sy, ex, ey)) {
				printNode(sx, sy);
				return true;
			} else {
				mark[sx + 1][sy] = 0;
			}
		}
		if (sy + 1 < n && mark[sx][sy + 1] == 0 && m[sx][sy + 1] == 1) {
			mark[sx][sy + 1] = 1;
			if (findPath(m, n, sx, sy + 1, ex, ey)) {
				printNode(sx, sy);
				return true;
			} else {
				mark[sx][sy + 1] = 0;
			}
		}
		return false;
	}

	static void printNode(int x, int y) {
		System.out.println(x + " " + y);
	}

	/**
	 * 搜狗二面：1.递归方法实现单链表逆置
	 */
	static public ListNode reverseList(ListNode head, int dep) {
		if (head.next == null) {
			return head;
		}
		ListNode res = reverseList(head.next, dep + 1);
		head.next.next = head;
		if (dep == 0) {
			head.next = null;
		}
		return res;
	}

	static public void printList(ListNode head) {
		while (head != null) {
			System.out.print(head.val + " ");
			head = head.next;
		}
		System.out.print("\n");
	}

	/**
	 * 搜狗二面：2.输入二叉树的前序和中序遍历，输出后序遍历
	 */
	static public void postTrav(int[] pre, int[] mid, int pLeft, int pRight, int mLeft, int mRight) {
		int middle = findMid(pre[pLeft], mid, mLeft, mRight);
		if (middle > mLeft) {
			postTrav(pre, mid, pLeft + 1, middle - mLeft + pLeft, mLeft, middle - 1);
		}
		if (middle < mRight) {
			postTrav(pre, mid, pRight - (mRight - middle) + 1, pRight, middle + 1, mRight);
		}
		System.out.print(pre[pLeft] + " ");
	}

	static public int findMid(int p, int[] mid, int left, int right) {
		for (int i = left; i <= right; i++) {
			if (mid[i] == p) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 美团笔试：给出面值为1，5，10，20，50，100的钱，当输入一个数值时，输出凑出这个数值的方法个数
	 * 不会。查了下答案，用动规思想，从小到大构建二维数组，相当于循环，遍历表，然后查表。
	 */

	public static int compute(int coins[], int coinKinds, int sum) {
		// 2-D array : dp[coinKinds+1][sum+1] = {0};
		int[][] dp = new int[(coinKinds + 1)][sum + 1];

		for (int i = 0; i <= coinKinds; ++i) {
			for (int j = 0; j <= sum; ++j) {
				dp[i][j] = 0;
			}
		}

		// init: dp[i][0] = 1; i = 0, 1, 2 ..., coinKinds
		// Notice: dp[0][0] must be 1, althongh it make no sense that
		// using 0 kinds of coins construct 0 has one way. but it the foundation
		// of iteration. without it everything based on it goes wrong
		for (int i = 0; i <= coinKinds; ++i) {
			dp[i][0] = 1;
		}

		// iteration: dp[i][j] = sum(dp[i-1][j - k*coins[i-1]])
		// k = 0, 1, 2, ... , j / coins[i-1]
		for (int i = 1; i <= coinKinds; ++i) {
			for (int j = 1; j <= sum; ++j) {
				dp[i][j] = 0;
				for (int k = 0; k <= j / coins[i - 1]; ++k) {
					dp[i][j] += dp[i - 1][j - k * coins[i - 1]];
				}
			}
		}

		return dp[coinKinds][sum];
	}

	/**
	 * 计算你能获得的最大收益
	 * 
	 * @param prices
	 *            Prices[i]即第i天的股价
	 * @return 整型
	 */
	public static int calculateMax(int[] prices) {
		if (prices == null || prices.length == 0) {
			return 0;
		}

		int[] dpl = new int[prices.length];
		int[] dpr = new int[prices.length];

		int minPrice = prices[0];
		int maxPrice = prices[prices.length - 1];

		dpl[0] = 0;
		dpr[prices.length - 1] = 0;
		// 从左向右
		for (int i = 1; i < prices.length; ++i) {
			if (prices[i] - minPrice > dpl[i - 1]) {
				dpl[i] = prices[i] - minPrice;
			} else {
				dpl[i] = dpl[i - 1];
				if (prices[i] < minPrice) {
					minPrice = prices[i];
				}
			}
		}

		// 从右向左
		for (int i = prices.length - 2; i >= 0; --i) {
			if (maxPrice - prices[i] > dpr[i + 1]) {
				dpr[i] = maxPrice - prices[i];
			} else {
				dpr[i] = dpr[i + 1];
				if (prices[i] > maxPrice) {
					maxPrice = prices[i];
				}
			}
		}
		int max = 0;
		for (int i = 0; i < prices.length; i++) {
			max = Math.max(dpl[i] + dpr[i], max);
		}
		return max;
	}
}
