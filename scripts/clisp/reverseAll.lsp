(defun makelist (x) (cons x nil)) 

(defun my-append (A1 A2)
	(cond
		((null A1) A2)
		(t (cons (car A1) (my-append (cdr A1) A2))))
)

(defun reverseAll (R)
	(cond 
		((atom R) R)
		(t (my-append 
			(reverseAll (cdr R))
			(makelist(reverseAll(car R)))
		   )	
		)	
	)
)

