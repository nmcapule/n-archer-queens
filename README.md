# N-Queens + no-3-queens-in-a-straight-line

This is an implementation of an n-queens solver, but with additional constraint
that there should be no 3 or more queens that align in a straight line.

## Requirements

- Option 1: Using `docker` and `make`, you probably need only to install docker.
- Option 2: Using `gradle` + `jdk15`. I used a few features like multi-line
  strings that is only in newer versions of the JDK.

## How to run

- Option 1: Using `docker` and `make`, run:

  ```sh
  # To run the solver with default arguments:
  $ make docker-gradle ARGS="run"
  # To run the solver with some arguments:
  $ make docker-gradle ARGS="run --args=\"-x -agradient 1000\""
  ```

- Option 2: Using `gradle` + `jdk15` in Linux:

  ```sh
  # To run the solver with default arguments:
  $ ./gradlew run
  # To run the solver with some arguments:
  $ ./gradlew run --args="-x -agradient 1000"
  # To run the tests:
  $ ./gradlew run --info
  ```

Here's how the `--help` command looks like:

```sh
Usage: x3nqueens [-hpVx] [-a=<algorithm>] <boardSize>
Solves n-queens problem with additional constraint that no 3 queens in a
straight line should exist.

      <boardSize>          Size of the board to solve. Must be >= 8 (or >= 4 if
                             3-queens-in-a-line check is disabled).

  -a, --algorithm=<algorithm>
                           backtracking|gradient
  -h, --help               Show this help message and exit.
  -p, --print-board        Prints the solution as a board in the output.
  -V, --version            Print version information and exit.
  -x, --no-3queens-check   Disables 3-queens-in-a-line check.
```

## Implementation

### Algorithm: backtracking

This implementation is the naive solution to n-queens.

The solver places queens from top to bottom, ensuring that each placed queen
don't conflict with the already placed queens from above. If there's no more
column available in the current row, it reverts back to the previous row above
and move the queen in that row again.

- Pros: Pretty straightforward.
- Cons: Pretty naive.

### Algorithm: gradient

This implementation is based on the paper from 1990,
[**A Polynomial Time Algorithm for the N-Queens Problem**](https://fizyka.umk.pl/~milosz/AlgIILab/10.1.1.57.4685.pdf).

The idea is to incrementally swap position of queens until we reach a solution
where there are no conflicting queens.

- Pros: This works really well on the traditional n-queens problem.
- Cons: It sucks on n-queens + no-3-queens-in-a-straight-line constraint -- even
  worse than the backtracking algorithm. It probably can still be optimized
  further.

### Detecting 3 (or more) queens in a straight line

To check if a newly-placed queen is in conflict with 2 other queens in a
straight line, we check for the slope of all previously placed queens over the
newly placed queen.

If there are at least 2 previous queens that have the same slope, then we assume
that they create a straight line with the newly placed queen.

For example, where `x` is the place of the queens in the board:

```
 . x . . . .
 . . . x . .
 . . . . . x
 x . . . . .
 . . x . . .
 . . . . x .
```

If we check for the 3rd queen at position `(x,y)=(5,2)`, we find out that it
produce same slopes with 2 other queens:

```
Q1/Q3 = (1-5)/(0-2) = 2
Q2/Q3 = (3-5)/(1-2) = 2
Q4/Q3 = (0-5)/(3-2) = -5
Q5/Q3 = (2-5)/(4-2) = -1.5
Q6/Q3 = (4-5)/(5-2) = -0.3333
```

This means that Q1-Q2-Q3 form a straight line.

## Notes about setting up Java in my machine

### Initial attempt

I am currently developing on VS Code remote, with Linux as a host. I prefer not
to install anything so I set up a docker command just to invoke gradle:

```Makefile
docker-gradle:
	docker run --rm -u gradle \
		-v "$(PWD)":/home/gradle/project \
		-w /home/gradle/project \
		gradle gradle $(ARGS)
```

### Setup w/ VS Code

Unfortunately, VS Code's extension for Java really requires a locally installed
JDK, so I had no choice but to use my Linuxbrew to install the latest OpenJDK:

```sh
$ brew install openjdk
```

This is not enough, since VS Code's Java extension can't detect Linuxbrew's
OpenJDK automatically. I had to find out where JDK home is with:

```sh
$ dirname $(dirname $(readlink -f $(which javac)))
```

And put it into my `java.home` settings in VS Code:

```json
{
  "java.home": "/home/linuxbrew/.linuxbrew/Cellar/openjdk/15.0.1/libexec"
}
```

The VS Code Java extension has a built-in auto-formatter. That's neat. It only
activates if the Java Language Server is set to Standard mode instead of
Lightweight mode however.

### Invocation

Running `gradle` with Docker is really slow. So I tried running `gradlew run`
and it installed `gradle` locally anyways -- at-least it's not system-wide
(inside `$PWD/gradle` folder).

Now I could run gradle commands either with:

```sh
# Using gradlew
$ gradlew run
# Using docker
$ make docker-gradle ARGS="run"
```

Pretty neat to explore actually. By the way, `gradlew` still needs a Java
install. If you only got Docker, just use the Docker invocation.
