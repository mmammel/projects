# Singly Linked List Definition
class ListNode:
  def __init__(self, val=0, next=None):
    self.val = val
    self.next = next

  def toString(self):
    first = True
    walker = self
    str = ""
    while walker is not None:
      if not first:
        str = str + "->"
      else:
        first = False 

      str = str + f"{walker.val}"
      walker = walker.next

    return str

def solve(head):
  if head is not None:
    tempNode = ListNode(val=head.val)
    while head.next is not None:
      tempNode = ListNode(val=head.next.val, next=tempNode)
      head = head.next
  else:
    tempNode = None

  return tempNode

node = ListNode( val=123 )
node2 = ListNode( val=234, next=node )
node3 = ListNode( val=345, next = node2)

print(node3.toString())
print(solve(node3).toString())
