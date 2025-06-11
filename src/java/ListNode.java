public class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    @Override
    public String toString() {
        return "val=" + val +
                ", " + next +
                '}';
    }

    public static ListNode head() {
        ListNode head = new ListNode(1);
        ListNode tail = head;
        for (int i = 2; i <= 5; i++) {
            tail = (tail.next = new ListNode(i));
        }
        return head;
    }
}
