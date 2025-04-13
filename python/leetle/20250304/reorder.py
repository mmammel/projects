class ListNode:
  def __init__(self, val=0, next=None):
    self.val = val
    self.next = next

def solve(head):
  prev = None
  walker = head
  while walker.next is not None:
    print(f"{walker.val}")
    prev = walker
    walker = walker.next

  # next is last
  prev.next = None
  walker.next = head.next
  head.next = walker
  return head

def printList(l):
  arr = []
  node = l
  while node is not None:
    arr.append(node.val)
    node = node.next

  print(arr)

def buildList(arr):
  head = None
  currNode = None
  tempNode = None
  for val in arr:
    tempNode = ListNode(val,None)
    if currNode is None:
      currNode = tempNode
      head = currNode
    else:
      currNode.next = tempNode
      currNode = tempNode
  return head
  

l = buildList([2,3,4,5,6])
printList(l)
l = solve(l)
printList(l)
