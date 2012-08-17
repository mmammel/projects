(defun len (L)
    (cond
        ((null L) 0)
        (t (+ 1 (len(cdr L))))
    )
)

(defun mymerge(L R)
  (cond
    ((null R) L)
    ((> (length L) (length R)) (cons (car L) (mymerge (cdr L) R)))
    ((>= (length R) (length L)) (cons (car R) (mymerge L (cdr R))))
  )
)
