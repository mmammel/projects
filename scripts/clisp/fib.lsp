(defun fib (n)
	(cond
		((> 3 n) 1)
		(t (+ (fib(- n 1)) (fib(- n 2))))
	)
)

