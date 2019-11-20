What’s With the Namesti
What’s the relationship between the terms “contravariance”, “invari-
ance”, and “covariance” and these different kinds of functor?
If you recall from Section 1.6.1, variance affects subtyping, which is es-
sentially our ability to use a value of one type in place of a value of
another type without breaking the code.
Subtyping can be viewed as a conversion. If B is a subtype of A , we can
always convert a B to an A .
Equivalently we could say that B is a subtype of A if there exists a func-
tion A => B . A standard covariant functor captures exactly this. If F is
a covariant functor, wherever we have an F[A] and a conversion A =>
B we can always convert to an F[B] .
A contravariant functor captures the opposite case. If F is a contravari-
ant functor, whenever we have a F[A] and a conversion B => A we can
convert to an F[B] .
Finally, invariant functors capture the case where we can convert from
F[A] to F[B] via a function A => B and vice versa via a function B =>
A