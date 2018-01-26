# Open Source Project (name tbd.)
## Mike Ren
### CSC630, Winter 2018

#### Overview
The purpose of this project is to create a library to store geometric objects algebraically. Geometric properties can then be easily discovered and verified algebraically by program. The coordinate system used is *barycentric coordinates* with respect to a triangle. In particular, such a system allows us to work with points at infinity so the entire real projective plane can be modeled.

#### Resources
Contributing may require a solid background in Euclidean and projective geometry (particularly Olympiad geometry), algebra, and familiarity with barycentric coordinates. Here are some resources to learn more about those:
* [Evan Chen and Max Schindler's article on barycentric coordinates on Olympiad Geometry](http://s3.amazonaws.com/aops-cdn.artofproblemsolving.com/resources/articles/bary.pdf) provides an overview of the technique to solve Olympiad geometry problems and the framework for this project.
* Evan Chen and Max Schindler also have written an [abridged version](http://web.evanchen.cc/handouts/bary/bary-short.pdf) with relevant formulas and properties for looking up.
* [Circles in barycentric coordinates](https://pdfs.semanticscholar.org/4563/00961af8c3ae6fc53a07b1dd400876b73940.pdf) by Vladimir Volenec gives equations and properties of circles in barycentric coordinates.

#### Functionalities
Currently, most features of points, lines, and circles have been implemented, with homogenous integer polynomials representing the coordinates of these objects. In the most recent update, a rudimentary diagram drawing tool has been implemented with a diagram of a triangle and its nine-point-circle and Euler line contained in `GeoScreen.java`. Eventually, we want to build the functionalities of an app like Geogebra into it.
