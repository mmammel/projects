# Binary Tree Definition
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#        self.right = right

def solve(root):
  maxDepth = 0
  return solveInner(root)    

def solveInner(node):
  d = 0
  if node is not None:
    d = 1
    ld = solveInner(node.left)
    rd = solveInner(node.right)

    if ld > rd:
      d = d + ld
    else:
      d = d + rd
  return d
