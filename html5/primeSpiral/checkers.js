/**
 * New Checker idea:
 *   digits of numbers base N
 *    base 10
 *    1 -> x 1
 *    2 -> x x 2
 *    3 -> x x x 3
 *    ...
 *    10 -> x 1 0
 *    11 -> x 1 x 1
 *     base 8
 *    8 -> x 1 0
 *    9 -> x 1 x 1
 *    17 -> x x 2 x 1
 */  
 

/**
 * checkers return an object like this:
 * 
 * {
 *   draw: boolean,
 *   turn: boolean
 * }
 */
function isPrime( n ) {
  var retVal = true;
  if( n <= 1 ) {
    retVal = false;
  } else if( n == 2 ) {
    retVal = true;
  } else if( n % 2 == 0 ) {
    retVal = false;
  } else {
    var s = Math.sqrt( n );
    var d = 3;
    while( d <= s ) {
      if( n % d == 0 ) {
        retVal = false;
        break;
      }
      d+=2;
    }
  }
  return retVal;
}

// var primeMask = new BitMask(80000000);
// for( var i = 0; i < 80000000; i++ ) {
//   if( isPrimeInit(i) ) primeMask.setBit( i );
// }

// function isPrime( n ) {
//   return primeMask.isSet(n);
// }


let checkers = [
  {
    id: "simple",
    name : "Simple",
    description : "Iterate once, turn.  Twice, turn.  Three times, turn. Etc.",
    default: true,
    stateObj : {
      next: 1,
      counter : 0
    },
    checker : function( state, n ) {
      var retVal = {
        draw: false,
        turn: false
      };
      if( state.counter == state.next ) {
        retVal.draw = true;
        retVal.turn = true;
        state.next++;
        state.counter = 0;
      } else {
        state.counter++;    
      }

      return retVal;
    }
  },
  {
    id: "VariableTurnIncrease",
    name : "Variable Turn Increase",
    description : "Simple, but the checker determines the turn increase",
    stateObj : {
      next: 1,
      counter : 0
    },
    checker : function( state, n ) {
      var retVal = {
        draw: false,
        turn: false,
        turnIncrease: 0.0
      };
      if( state.counter == state.next ) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnIncrease = (n%100)*.001;
        state.next++;
        state.counter = 0;
      } else {
        state.counter++;    
      }

      return retVal;
    }
  },
  {
    id: "simpleHitch",
    name : "Simple with a hitch",
    description : "Just like simple but only turn if divisible by given X",
    stateObj : {
      next: 1,
      counter : 0
    },
    checker : function( state, n, x ) {
      var retVal = {
        draw: false,
        turn: false
      };
      if( state.counter == state.next ) {
        retVal.draw = true;
        if( state.counter % x == 0 ) retVal.turn = true;
        state.next++;
        state.counter = 0;
      } else {
        state.counter++;    
      }

      return retVal;
    },
    extraVars : [
      {
        id : "X",
        label : "Turn if divisible by: ",
        defaultVal: 2
      }
    ]
  },
  {
    id: "simplePrimes",
    name : "Simple with primes",
    description : "Iterate N times, if N is the next prime, draw and turn",
    stateObj : {
      next: 2,
      counter : 0
    },
    checker : function( state, n, x ) {
      var retVal = {
        draw: false,
        turn: false
      };
      if( state.counter == state.next ) {
        retVal.draw = true;
        retVal.turn = true;
        state.next = state.next + (state.next == 2 ? 1 : 2);
        while( !isPrime(state.next) ){ state.next += 2; };
        state.counter = 0;
      } else {
        state.counter++;    
      }

      return retVal;
    }
  },
  {
    id : "pi",
    name : "Pi Digits",
    description : "After iterating (the next digit of pi) times, plot a point and turn",
    stateObj : {
      idx: 0, next: 3, piidx: 0
    },
    checker : function(state, n ) {
      var retVal = {
        draw: false,
        turn: false
      };
      if( state.idx == state.next ) {
        state.idx = 0;
        state.next = parseInt( piString.charAt( state.piidx++ ) );
        retVal.draw = true;
        retVal.turn = true;
      } else {
        state.idx++;
      }
  
      return retVal;
    }
  },
  {
    id : "multipleOf",
    name : "Multiple of X",
    description : "If the iteration i is a multiple of X, plot a point and turn",
    stateObj : { },
    checker : function(state, n, x) { 
      var retVal = {
        draw: false,
        turn: false
      };
      if( n % x == 0 ) {
        retVal.draw = true;
        retVal.turn = true;
      }
      return retVal;
    },
    extraVars : [
      {
        id : "x",
        label : "Multiple of what?",
        defaultVal : 16
      }
    ]
  },
  {
    id : "geometric",
    name : "Simple Geometric",
    description : "Increase the simple target by a constant multiple.  Less than 1 will cause issues, greater than 1 is boring.  Look at 1.0005 at 20, 30, 45, 60, 90, 270 for some bizarre surprises",
    stateObj : { 
      next: 1,
      counter: 0
    },
    checker : function(state, n, x) { 
      var retVal = {
        draw: false,
        turn: false
      };
      if( state.counter >= state.next ) {
        retVal.draw = true;
        retVal.turn = true;
        state.next = x*state.next;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars : [
      {
        id : "x",
        label : "Geometric Multiplier",
        defaultVal : 2
      }
    ]
  },
  {
    id : "arithmetic",
    name : "Simple Arithmetic",
    description : "Increase the simple target by a constant addition, really just acts like a zoom for simple, unless you do reaaaally small additives.",
    stateObj : { 
      next: 1,
      counter: 0
    },
    checker : function(state, n, x) { 
      var retVal = {
        draw: false,
        turn: false
      };
      if( state.counter >= state.next ) {
        retVal.draw = true;
        retVal.turn = true;
        state.next = x + state.next;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars : [
      {
        id : "x",
        label : "Arithmetic Additive",
        defaultVal : 2
      }
    ]
  },
  {
    id : "simplePi",
    name : "Simple Pi",
    description : "Iterate N times, if N is the next digit of pi, draw and turn",
    stateObj : { next: 3, counter: 0, piIdx: 0},
    checker : function(state, n, x) {
      var retVal = {
        draw: false,
        turn: false
      };
      if( state.next == state.counter ) {
        state.next = parseInt(piString.charAt(++state.piIdx));
        state.counter = 0;
        retVal.draw = true;
        retVal.turn = true;
      } else {
        state.counter++;
      }  
      return retVal;
    }
  },
  {
    id : "simpleNaturalLog",
    name : "Simple Natural Log",
    description : "Same as simple, but use: 10*ln(next)",
    stateObj : { next: 1, counter: 0 },
    checker : function(state, n, x) {
      var retVal = {
        draw: false,
        turn: false
      };
      if( state.counter >= 10*(Math.log(state.next))) {
        state.next++;
        state.counter = 0;
        retVal.draw = true;
        retVal.turn = true;
      } else {
        state.counter++;
      }  
      return retVal;
    }
  },
  {
    id : "piSpecific",
    name : "Pi Digit = X",
    description : "Each iteration i is a digit of pi - if the digit equals X, plot a point and turn",
    stateObj : { },
    checker : function(state, n, x) {
      var retVal = {
        draw: false,
        turn: false
      };
      var val = parseInt( piString.charAt( n ) );
      if( val == x ) {
        retVal.draw = true;
        retVal.turn = true;
      } 
  
      return retVal;
    },
    extraVars : [
      {
        id : "x",
        label : "Which digit?",
        values : [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ],
        defaultVal : 0
      }
    ]
  },
  {
    id: "piPrime",
    name : "Pi Digit is Prime",
    description : "Each iteration i is a digit of pi - if the digit is prime, plot a point a turn",
    stateObj : {
    },
    checker : function(state, n) {
      var retVal = {
        draw: false,
        turn: false
      };
      var val = parseInt( piString.charAt( n ) );
      if( val == 2 || val == 3 || val == 5 || val == 7 ) {
        retVal.draw = true;
        retVal.turn = true;
      } 
  
      return retVal;
    }
  },
  // {
  //   id: "multiple",
  //   name : "Increasing Multiple",
  //   description: "Starting with n=1, every time the iteration i is a multiple of n plot a point and turn, and increase n by 1",
  //   stateObj : {
  //     num: 1, val: 1
  //   },
  //   checker : function(state, n) {
  //     var retVal = false;
  //     if( multipleOf(n, state.num )) {
  //       retVal = true;
  //       state.num++;
  //     }
  //     return retVal;
  //   }
  // },
  // {
  //   id: "gauss",
  //   name : "Gauss Numbers",
  //   description : "As the iteration hits each gauss number plot a point and turn.",
  //   stateObj : {num: 1, val: 1},
  //   checker: function(state, n) {
  //     var retVal = false;
  //     if( n == state.val ) {
  //       retVal = true;
  //       state.num++;
  //       state.val = ( state.num * (state.num - 1) / 2 );
  //     }
  
  //     return retVal;
  //   }
  // },
  {
    id: "prime",
    name : "Primes",
    description : "If the iteration is prime plot a point and turn",
    stateObj : {},
    checker : function(state, n) {
      var retVal = {
        draw: true,
        turn: true
      };
      if( !isPrime(n) ) {
        retVal.draw = false;
        retVal.turn = false;
      }
      return retVal;
    }
  },
  {
    id: "fib",
    name: "Fibonacci",
    description: "If the iteration is a fibonacci number, plot a point and turn",
    stateObj : {num0: 0, num1: 1, val: 1},
    checker : function(state, n) {
      var retVal = {
          draw: false,
          turn: false
      };
      if( state.val == (state.num0 + state.num1) ) {
        retVal.draw = true;
        retVal.turn = true;
        state.num0 = state.num1;
        state.num1 = state.val;
        state.val = 0;
      }
      state.val++;

      return retVal;
    }
  },
  {
    id: "random",
    name: "Random",
    description: "Randomly plot a point and turn, if rand() <= v (0 < v < 1)",
    stateObj : {},
    checker : function(state, n, v ) {
      var retVal = {
        draw: false,
        turn: false
      };
      if( Math.random() <= v ) {
        retVal.draw = true;
        retVal.turn = true;
      }
      return retVal;
    },
    extraVars : [
      {
        id : "v",
        label : "Random Cutoff (0 <= v < 1)",
        defaultVal : .5
      }
    ]
  }
];

