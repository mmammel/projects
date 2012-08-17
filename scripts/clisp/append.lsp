(defun my-append (A1 A2)
	(cond
		((null A1) A2)
		(t (cons (car A1) (my-append (cdr A1) A2))))
)


