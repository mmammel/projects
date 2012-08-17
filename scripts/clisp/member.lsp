(defun is-member (A1 X)
	(cond
		((null A1) (princ "Not a member of the list"))
		((= (car A1) X) (princ "Is a member of the list"))
		(t (is-member (cdr A1) X)))
)


