(defun inlist(L I)
  (cond
    ((null L) nil)
    ((= (car L) I) t)
    (t (inlist (cdr L) I))
  )
)

(defun makeset(L)
  (cond
    ((null L) L)
    ((inlist (cdr L) (car L)) (makeset (cdr L)))
    (t (cons (car L) (makeset (cdr L))))
  )
)

(defun int(L M)
  (set-intersect (makeset L) (makeset M))
)

(defun set-intersect(L M)
  (cond
    ((null L) nil)
    ((inlist M (car L)) (cons (car L) (set-intersect (cdr L) M)))
    (t (set-intersect (cdr L) M))
  )
)

(defun un(L M)
  (makeset (myappend (makeset L) (makeset M)))
)

(defun myappend(L M)
  (cond
    ((null L) M)
    (t (cons (car L) (myappend (cdr L) M)))
  )
)
