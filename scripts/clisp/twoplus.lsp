(defun two (L)
	(cond
		((atom L) nil) 
		((atom (cdr L)) nil )
		(t t)
	)
)

