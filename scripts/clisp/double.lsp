(defun double(A)
	(cond
		((null A) nil)
		(t (cons (car A) (cons (car A) (double (cdr A)))))
	)
)


