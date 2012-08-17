(defun del(L I)
  (cond
    ((null L) L)
    ((= (car L) I) (del (cdr L) I))
    (t (cons (car L) (del (cdr L) I)))
  )
)
 
