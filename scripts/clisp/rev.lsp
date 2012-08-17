(defun makelist(x) (cons x nil))

(defun my-append (A1 A2)
	(cond
		((null A1) A2)
		(t (cons (car A1) (my-append (cdr A1) A2))))
)

(defun my-reverse (R)
	(cond 
		((null R) R)
		(t (my-append (my-reverse (cdr R)) (makelist(car R)))
	)
))

