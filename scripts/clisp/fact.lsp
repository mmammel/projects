(defun fact (n)
	(cond
		((= 1 n) 1)
		(t (* n (fact(- n 1))))
	)
)

