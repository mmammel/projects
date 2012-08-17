(defun len (L)
    (cond
        ((null L) 0)
        (t (+ 1 (len(cdr L))))
    )
)

