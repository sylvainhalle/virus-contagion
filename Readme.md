A simple virus contagion simulator
==================================

In March 2020, the Washington Post published an online
[paper](https://www.washingtonpost.com/graphics/2020/world/corona-simulator)
that showed, through different simulations, how the spread of a virus evolves
under different circumstances --such as the now well-known *social distancing*.
Each of the simulations was made of a rectangular area, in the user's browser,
where small circles could move around, bump into (and infect) each other, and
eventually recover.

Depending on how many of them could move, the total and peak number of infected
circles would follow different curves, such as the ones shown below.

![Different curves](https://www.washingtonpost.com/graphics/2020/world/corona-simulator/img/sig-gif.gif)

This project is an attempt at reproducing this simulator, by using a variety
of software tools developed at [Laboratoire d'informatique formelle](https://liflab.ca),
a research lab in Computer Science from [Université du Québec à Chicoutimi](https://www.uqac.ca)
in Québec, Canada. Most notably, the project showcases two important libraries
developed at LIF:

- The [BeepBeep](https://liflab.github.io/beepbeep-3) event stream processing
  engine
- The [Synthia](https://github.com/liflab/synthia) data structure generator

The rest of this Readme explains how the simulator has been built.

[See a video](https://youtu.be/ZlN3X-xJJL0) of the simulator running.

Premise
-------

The Post's simulator works as follows:

1. Circles are randomly placed in a rectangular "arena" and are initially moving
   in randomly selected directions. A varying number of circles can be made
   fixed: they don't move for the entire duration of the simulation.
2. A single circle is initially marked as "infected" (brown); all the other
   circles are "healthy" (light blue).
3. When two circles collide, they bounce off each other; moreover, if one is
   infected and the other is healthy, the healthy one becomes infected.
4. After a fixed amount of time, an infected circle becomes "recovered"
   (purple); it can neither become infected again, nor make other circles
   infected.

Simulating the "arena"
----------------------

In our project, the management of the physics of colliding circles is done
by the package `virussim.physics`. Inside this package, the `Ball` class
implements a simple two-dimensional "ball", with a position and a speed vector,
that can compute elastic collisions with other balls. The `Arena` is just a
set of balls; its method `update` acts like a "tick" that moves the simulation
one step forward: it goes through all `Ball` objects, updates their state, and
determines if any collision occurs.

Most of the code in this package is borrowed and adapted from an answer on
[StackOverflow](https://stackoverflow.com/q/345838). One adaptation is that the
arena can be asked to output its current state, in the form of a `Map` linking
the unique ID of each ball with the corresponding ball instance.

The `virussim` package defines the `Patient` class, which is a descendent of
`Ball` that can catch a virus when in contact with another infected ball. To
this end, `Patient` has a member field keeping its `Health` state.

Generating the initial state
----------------------------

The initial state of the arena is created by making multiple random "choices"
for each patient. In order to generate this state, we make extensive use of the
`Picker` interface provided by the [Synthia](https://github.com/liflab/synthia)
library. A "picker" is any object that implements a method called `pick()`,
which, when called, returns an object of a certain type. For example,
`RandomInteger` is a picker that returns a randomly selected integer number on
every call to its `pick()` method; the same for `RandomFloat`, `RandomBoolean`,
and so on.

### Initial position

However, Synthia can generate objects that go beyond random scalars. To generate
each patient's initial position, we use the `PrismPicker`: given two pickers
p1 and p2, this picker generates a 2D vector where the first coordinate is
given by asking p1, and the second coordinate is given by asking p2.

Depending on how these two values are picked, this can correspond to a different
placement of the circles in the arena. Our program provides two options:

- In the first case, p1 and p2 perform an affine transformation of a uniformly
  distributed `float` in the interval [0,1]; in such a case, the circles are
  scattered uniformaly over the arena.
- In the second case, p1 and p2 perform a different affine transformation over
  a `float` that follows a Gaussian distribution. This causes the circles to be
  clustered towards the center of the arena.

### Initial velocity

Similarly, the initial direction of each patient is given by another 2D vector
specifying its velocity. However, for the sake of elegance, it would be
desirable that the speed of each circle be the same --that is, their velocity
vector can have an arbitrary orientation, but must have the same *modulus*. This
can be done using Synthia's `HyperspherePicker`, which does exactly that: it
takes as input a vector length, and a picker that provides an arbitrary float
value. This value is interpreted as an angle (in radians) in a polar coordinate
system, thus generating vectors of a fixed modulus but with a potentially
varying angle.

### Fixed or moving?

Another part of the patient's state that must be specified is whether its
associated circle is fixed or moving. The Post's simulations test various
proportions of moving circles. In our setup, the decision whether a patient is
moving or not is provided by a straightforward `RandomBoolean`, which works like
a *biased* coin toss; indeed, we can specify the probability that it returns
true (meaning the patient is fixed) to whatever fraction we wish.

<!-- :mode=markdown:maxLineLen=80: -->