# Definition for singly-linked list.


class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None


class Intersection:

    def findIntersection(self, l1: ListNode, l2: ListNode) -> ListNode:

        # Placeholders to avoid
        currNodeA = l1
        currNodeB = l2

        size1 = 0
        size2 = 0

        tailNodeA = None
        tailNodeB = None

        if currNodeA is None or currNodeB is None:
            return None

        while currNodeA is not None:
            size1 += 1
            currNodeA = currNodeA.next
            if currNodeA is not None and currNodeA.next is None:
                tailNodeA = currNodeA

        while currNodeB is not None:
            size2 += 1
            currNodeB = currNodeB.next
            if currNodeB is not None and currNodeB.next is None:
                tailNodeB = currNodeB

        if tailNodeA != tailNodeB:
            # Lists don't intersect
            return None

        diff = abs(size1 - size2)

        currNodeA = l1
        currNodeB = l2

        while diff > 0:
            if size1 > size2:
                currNodeA = currNodeA.next
            else:
                currNodeB = currNodeB.next
            diff -= 1

        while currNodeA is not None and currNodeB is not None:

            if currNodeA == currNodeB:
                return currNodeA

            currNodeA = currNodeA.next
            currNodeB = currNodeB.next

        return None


def main():

        list1 = ListNode("Sam")

        l1node2 = ListNode("Jay")
        list1.next = l1node2

        l1node3 = ListNode("Yuri")
        l1node2.next = l1node3

        l1node4 = ListNode("Roy")
        l1node3.next = l1node4

        l1node5 = ListNode("Cece")
        l1node4.next = l1node5

        list2 = ListNode("Emilia")

        l2node2 = ListNode("Olive")
        list2.next = l2node2

        l2node3 = ListNode("May")
        l2node2.next = l2node3

        l2node4 = ListNode("Chloe")
        l2node3.next = l2node4

        l2node4.next = l1node5

        node = Intersection().findIntersection(list1, list2)
        if node is not None:
            print(node.val)
        else:
            print(None)


if __name__ == '__main__':
    main()

