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

function getCollatzConvergenceNum( num ) {
  // return how many iterations it takes to get to 1
  let val = num;
  let count = 0;
  while( val != 1 ) {
    count++;
    val = val % 2 == 0 ? val / 2 : 3*val + 1;
  }

  return count;
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
  },{
    id: "simple3D",
    name: "Simple 3D",
    description: "Same draw/turn pattern as Simple, but also turns in the Z direction (φ) on every turn. Set the φ angle and enable 3D Orbit to watch the helix emerge.",
    is3D: true,
    stateObj: {
      next: 1,
      counter: 0
    },
    checker: function(state, n, x) {
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter == state.next) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.counter % x == 0;
        state.next++;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars: [{ id: "x", label: "Only turn Z when multiple of...", defaultVal: 1 }]
  },{
    id: "simpleGolden3D",
    name: "Simple Golden 3D",
    description: "Simple but increase each number by the golden ratio.",
    is3D: true,
    stateObj: {
      next: 1,
      counter: 0
    },
    checker: function(state, n, x) {
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter >= state.next) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.counter % x == 0;
        state.next = 1.618 * state.next;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars: [{ id: "x", label: "Only turn Z when multiple of...", defaultVal: 1 }]
  },{
    id: "gravity3D",
    name: "Gravity 3D",
    description: "Scale the distance for the next simple target directly with the distance from the origin - closer to the origin, the shorter the next point will be.  Calculated by 2 + (x * ( d / (d + sc))) where sc is a scale factor, and x is a max distance.",
    is3D: true,
    stateObj: {
      next: 1,
      counter: 0
    },
    checker: function(state, n, x, sc, tz) {
      var retVal = {
        draw: false,
        turn: false,
        turnZ: false
      };
      if (state.counter >= state.next) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.counter % tz == 0;
        var c = state.coords;
        var d = Math.sqrt(c[0]*c[0] + c[1]*c[1] + c[2]*c[2])
        var nextDistance = 2 + (x * ( d / (d + sc)));
        if( nextDistance > x ) nextDistance = x;
        state.next = nextDistance;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    },
    extraVars: [
       { id: "x", label: "Max distance: ", defaultVal: 1000 },
       { id: "sc", label: "Scaling factor: ", defaultVal: 100 },
       { id: "tz", label: "Only turn Z on multiples of: ", defaultVal: 1 }
    ]
  },{
    id: "simplegauss",
    name : "Simple Gauss",
    description : "Simple, but iterate over the Gause numbers: n(n+1)/2",
    stateObj : {
      seed: 2,
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
        state.next = (state.seed * ( state.seed + 1 ) ) / 2;
        state.seed++;
        state.counter = 0;
      } else {
        state.counter++;
      }

      return retVal;
    }
  },{
    id: "everything",
    name : "Everything",
    description : "Always draw a dot and turn, just to see what happens.  Capped at 1000 points",
    stateObj : {
      limit: 1000
    },
    checker : function( state, n, B ) {
      retVal = {
        draw: true,
        turn: true
      };

      if( B != null && B !== 'Always turn and draw' ) {
        if( n % 2 == 0 ) {
          retVal.turn = false;
          if( B === 'No turn or draw on evens' ) {
            retVal.draw = false;
          }
        }
      }

      if( n > state.limit ) {
        retVal.draw = false;
        retVal.turn = false;
      }

      return retVal;
    },
    extraVars : [
      {
        id : "B",
        label : "Choose behavior",
        values : [
          'Always turn and draw',
          'No turn on evens',
          'No turn or draw on evens'
        ],
        defaultVal : 'Always turn and draw'
      }
    ]
  },{
    id: "collatz",
    name : "Collatz",
    description : "Do simple but for the array of times it takes the numbers from 2-10000 to get to a power of 2, sorted.",
    default: false,
    stateObj : {
      arrayIdx: 0, 
      counter : 0
    },
    checker : function( state, n ) {
      var retVal = {
        draw: false,
        turn: false
      };

      //if( state.counter == collatzPowerOfTwoTimes[state.arrayIdx] ) {
      //if( state.counter == collatzPowerOfTwoTimesUnsorted[state.arrayIdx] ) {
      if( state.counter == collatz3077s[state.arrayIdx] ) {
        retVal.draw = true;
        retVal.turn = true;
        state.arrayIdx++;
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
    id: "simplePrimeHitch",
    name : "Simple with a prime hitch",
    description : "Just like simple but only turn if prime",
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
        if( isPrime( state.counter ) ) retVal.turn = true;
        state.next++;
        state.counter = 0;
      } else {
        state.counter++;
      }

      return retVal;
    }
  },
  {
    id: "baseDigits",
    name : "Base Digits",
    description : "For each number, put it into Base B, and then do simple for each digit of the converted number.",
    stateObj : {
      digitIdx: 0,
      counter: 0,
      currNumber: 0,
      digits: [0]
    },
    checker : function( state, n, B ) {
      var retVal = {
        draw: false,
        turn: false
      };

      if( state.counter == state.digits[state.digitIdx] ) {
        retVal.draw = true;
        retVal.turn = true;
        state.digitIdx++;
        if( state.digitIdx == state.digits.length ) {
          // next number.
          state.currNumber++;
          var str = state.currNumber.toString(B);
          var tmp = str.split('');
          state.digits = [];
          for( var i = 0; i < tmp.length; i++ ) {
            state.digits.push(parseInt(tmp[i],B));
          }
          state.digitIdx = 0;
        }
        state.counter = 0;
      } else {
        state.counter++;    
      }

      return retVal;
    },
    extraVars : [
      {
        id : "B",
        label : "Which base?",
        values : [ 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 ],
        defaultVal : 10
      },
      {
        id: "currNumber",
        label : "Starting Value: ",
        defaultVal: 1,
        changeFunc : function(state, val) {
          state.currNumber = val;
        }
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
  },
  {
    id: "simplegauss3D",
    name: "Simple Gauss 3D",
    description: "Simple Gauss but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { seed: 2, next: 1, counter: 0 },
    checker: function(state, n) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter == state.next) {
        retVal.draw = true;
        retVal.turn = true;
        state.next = (state.seed * (state.seed + 1)) / 2;
        state.seed++;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "everything3D",
    name: "Everything 3D",
    description: "Everything but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { limit: 2000 },
    checker: function(state, n, B, X) {
      var retVal = { draw: true, turn: true, turnZ: false };
      if (B != null && B !== 'Always turn and draw') {
        if ( B === 'No turn on multiples of X' ) {
          if (n % X == 0) {
            retVal.turn = false;
          }
        } else if( B === 'No turn or draw on multiples of X' ) {
          if( n % X == 0) {
            retVal.turn = false;
            retVal.draw = false;
          }
        } else if( B === 'No turn on Prime' ) {
          if( isPrime(n) ) {
            retVal.turn = false;
          }
        }
      }
      if (n > state.limit) { retVal.draw = false; retVal.turn = false; }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [
      {
        id: "B",
        label: "Choose behavior",
        values: ['Always turn and draw', 'No turn on multiples of X', 'No turn or draw on multiples of X', 'No turn on Prime'],
        defaultVal: 'Always turn and draw'
      },
      {
        id: "X",
        label: "X:",
        defaultVal: 1
      }
    ]
  },
  {
    id: "collatz3D",
    name: "Collatz 3D",
    description: "Collatz but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { arrayIdx: 0, counter: 0 },
    checker: function(state, n) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter == collatz3077s[state.arrayIdx]) {
        retVal.draw = true;
        retVal.turn = true;
        state.arrayIdx++;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "VariableTurnIncrease3D",
    name: "Variable Turn Increase 3D",
    description: "Variable Turn Increase but also turns in the Z direction (φ) on every turn. Turn increase scales inversely with distance from the origin — tightly wound near the center (max 1.0) and gradually opening up as the spiral moves outward (min 0.001), producing a galaxy-like structure with a dense core and open arms. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { next: 1, counter: 0 },
    checker: function(state, n, mn, mx ) {
      var retVal = { draw: false, turn: false, turnZ: false, turnIncrease: 0.0 };
      if (state.counter == state.next) {
        retVal.draw = true;
        retVal.turn = true;
        var c = state.coords;
        var p = state.prevCoords == null ? [0.0,0.0,0.0] : state.prevCoords;
        //var dist = Math.sqrt((c[0]-p[0])*(c[0]-p[0]) + (c[1]-p[1])*(c[1]-p[1]) + (c[2]-p[2])*(c[2]-p[2]));
        var dist = Math.sqrt(c[0]*c[0] + c[1]*c[1] + c[2]*c[2]);
        retVal.turnIncrease = Math.max(mn, Math.min(mx, mx / (1 + 0.01 * dist)));
        state.next++;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [ { id: "minIncrease", label: "Minimum turn increase: ", defaultVal: 0 },
                 { id: "maxIncrease", label: "Maximum turn increase: ", defaultVal: .5 } ]
  },
  {
    id: "simpleHitch3D",
    name: "Simple with a hitch 3D",
    description: "Simple with a hitch but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { next: 1, counter: 0 },
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter == state.next) {
        retVal.draw = true;
        if (state.counter % x == 0) retVal.turn = true;
        state.next++;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [{ id: "X", label: "Turn if divisible by: ", defaultVal: 2 }]
  },
  {
    id: "simplePrimeHitch3D",
    name: "Simple with a prime hitch 3D",
    description: "Simple with a prime hitch but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { next: 1, counter: 0 },
    checker: function(state, n) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter == state.next) {
        retVal.draw = true;
        if (isPrime(state.counter)) retVal.turn = true;
        state.next++;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "baseDigits3D",
    name: "Base Digits 3D",
    description: "Base Digits but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { digitIdx: 0, counter: 0, currNumber: 0, digits: [0] },
    checker: function(state, n, B) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter == state.digits[state.digitIdx]) {
        retVal.draw = true;
        retVal.turn = true;
        state.digitIdx++;
        if (state.digitIdx == state.digits.length) {
          state.currNumber++;
          var str = state.currNumber.toString(B);
          var tmp = str.split('');
          state.digits = [];
          for (var i = 0; i < tmp.length; i++) state.digits.push(parseInt(tmp[i], B));
          state.digitIdx = 0;
        }
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [
      { id: "B", label: "Which base?", values: [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16], defaultVal: 10 },
      { id: "currNumber", label: "Starting Value: ", defaultVal: 1, changeFunc: function(state, val) { state.currNumber = val; } }
    ]
  },
  {
    id: "simplePrimes3D",
    name: "Simple with primes 3D",
    description: "Simple with primes but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { next: 2, counter: 0, last: 2 },
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter == state.next) {
        state.last = state.next;
        retVal.draw = true;
        retVal.turn = true;
        state.next = state.next + (state.next == 2 ? 1 : 2);
        while (!isPrime(state.next)) { state.next += 2; }
        // only turnZ on twin primes
        if( state.next - state.last == 2 ) retVal.turnZ = true;
        state.counter = 0;
      } else {
        state.counter++;
      }
      return retVal;
    }
  },
  {
    id: "pi3D",
    name: "Pi Digits 3D",
    description: "Pi Digits but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { idx: 0, next: 3, piidx: 0 },
    checker: function(state, n) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.idx == state.next) {
        state.idx = 0;
        state.next = parseInt(piString.charAt(state.piidx++));
        retVal.draw = true;
        retVal.turn = true;
      } else {
        state.idx++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "multipleOf3D",
    name: "Multiple of X 3D",
    description: "Multiple of X but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: {},
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (n % x == 0) { retVal.draw = true; retVal.turn = true; }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [{ id: "x", label: "Multiple of what?", defaultVal: 16 }]
  },
  {
    id: "geometric3D",
    name: "Simple Geometric 3D",
    description: "Simple Geometric but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { next: 1, counter: 0 },
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter >= state.next) {
        retVal.draw = true;
        retVal.turn = true;
        state.next = x * state.next;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [{ id: "x", label: "Geometric Multiplier", defaultVal: 2 }]
  },
  {
    id: "arithmetic3D",
    name: "Simple Arithmetic 3D",
    description: "Simple Arithmetic but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { next: 1, counter: 0 },
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter >= state.next) {
        retVal.draw = true;
        retVal.turn = true;
        state.next = x + state.next;
        state.counter = 0;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [{ id: "x", label: "Arithmetic Additive", defaultVal: 2 }]
  },
  {
    id: "simplePi3D",
    name: "Simple Pi 3D",
    description: "Simple Pi but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { next: 3, counter: 0, piIdx: 0 },
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.next == state.counter) {
        state.next = parseInt(piString.charAt(++state.piIdx));
        state.counter = 0;
        retVal.draw = true;
        retVal.turn = true;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "simpleNaturalLog3D",
    name: "Simple Natural Log 3D",
    description: "Simple Natural Log but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { next: 1, counter: 0 },
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.counter >= 10 * (Math.log(state.next))) {
        state.next++;
        state.counter = 0;
        retVal.draw = true;
        retVal.turn = true;
      } else {
        state.counter++;
      }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "simplePascal3D",
    name: "Simple Pascal 3D",
    description : "Starting at the top and reading left to right, do simple on Pascal's triangle",
    is3D: true,
    stateObj: {
      arr: [ 1 ],
      currIdx: 0,
      counter: 0
    },
    checker: function(state, n ) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if( state.counter == state.arr[ state.currIdx ] ) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = true;
        state.counter = 0;
        state.currIdx++;
      } else {
        state.counter++;
      }
      // build next array
      if( state.currIdx >= state.arr.length ) {
        //build the next array
        state.currIdx = 0;
        var newArr = [ 1 ];
        if( state.arr.length == 1 ) {
          newArr.push(1);
        } else {
          for( var i = 0; i < state.arr.length - 1; i++ ) {
            newArr.push( state.arr[i] + state.arr[i+1] );
          }
          newArr.push(1);
        }
        state.arr = newArr;
      }

      return retVal;
    }
  },
  {
    id: "piSpecific3D",
    name: "Pi Digit = X 3D",
    description: "Pi Digit = X but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: {},
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };
      var val = parseInt(piString.charAt(n));
      if (val == x) { retVal.draw = true; retVal.turn = true; }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [{ id: "x", label: "Which digit?", values: [0,1,2,3,4,5,6,7,8,9], defaultVal: 0 }]
  },
  {
    id: "piPrime3D",
    name: "Pi Digit is Prime 3D",
    description: "Pi Digit is Prime but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: {},
    checker: function(state, n) {
      var retVal = { draw: false, turn: false, turnZ: false };
      var val = parseInt(piString.charAt(n));
      if (val == 2 || val == 3 || val == 5 || val == 7) { retVal.draw = true; retVal.turn = true; }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "prime3D",
    name: "Primes 3D",
    description: "Primes but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: {},
    checker: function(state, n) {
      var retVal = { draw: true, turn: true, turnZ: false };
      if (!isPrime(n)) { retVal.draw = false; retVal.turn = false; }
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "fib3D",
    name: "Fibonacci 3D",
    description: "Fibonacci but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: { num0: 0, num1: 1, val: 1 },
    checker: function(state, n) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (state.val == (state.num0 + state.num1)) {
        retVal.draw = true;
        retVal.turn = true;
        state.num0 = state.num1;
        state.num1 = state.val;
        state.val = 0;
      }
      state.val++;
      retVal.turnZ = retVal.turn;
      return retVal;
    }
  },
  {
    id: "random3D",
    name: "Random 3D",
    description: "Random but also turns in the Z direction (φ) on every turn. Enable 3D Orbit to see the structure.",
    is3D: true,
    stateObj: {},
    checker: function(state, n, v) {
      var retVal = { draw: false, turn: false, turnZ: false };
      if (Math.random() <= v) { retVal.draw = true; retVal.turn = true; }
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [{ id: "v", label: "Random Cutoff (0 <= v < 1)", defaultVal: .5 }]
  },
  {
    id: "orbitalResonance",
    name: "Orbital Resonance",
    description: "Virtual planets orbit at golden-ratio frequency ratios (1 : φ : φ² : φ³). Each planet starts at an evenly spread initial phase so coherence begins near zero (maximum spread). As the bodies drift at incommensurable speeds, rare conjunction events (high coherence) trigger a draw; opposition events (low coherence) trigger a turn. Because powers of φ are the hardest irrationals to approximate by rationals, conjunctions cluster at Fibonacci-scaled intervals. Adjust Freq to change the orbital period; Bodies (2–4) changes how many planets participate.",
    stateObj: {
      freqs:  [1.0, 1.6180339887, 2.6180339887, 4.2360679774],
      phases: [0.0, 2.0944,        4.1888,        1.0472]
    },
    checker: function(state, n, j, k) {
      var numBodies = Math.max(2, Math.min(4, Math.round(k)));
      var cx = 0, cy = 0;
      for (var b = 0; b < numBodies; b++) {
        var angle = (n * j * state.freqs[b] + state.phases[b]) % (2 * Math.PI);
        cx += Math.cos(angle);
        cy += Math.sin(angle);
      }
      var coherence = Math.sqrt(cx * cx + cy * cy) / numBodies;
      return {
        draw: coherence > 0.72,
        turn: coherence < 0.28
      };
    },
    extraVars: [
      { id: "orFreq",   label: "Freq",   defaultVal: 0.15 },
      { id: "orBodies", label: "Bodies", defaultVal: 3 }
    ]
  },
  {
    id: "orbitalResonance3D",
    name: "Orbital Resonance 3D",
    description: "Orbital Resonance extended into full spherical orbits. Each planet starts at an evenly spread initial phase. The azimuthal angle uses the golden-ratio frequencies; the polar angle uses their inverses (1/φ^k), so the two coordinates are always incommensurable. Enable 3D Orbit to explore the nested shell structures.",
    is3D: true,
    stateObj: {
      freqs:  [1.0, 1.6180339887, 2.6180339887, 4.2360679774],
      phases: [0.0, 2.0944,        4.1888,        1.0472]
    },
    checker: function(state, n, j, k) {
      var numBodies = Math.max(2, Math.min(4, Math.round(k)));
      var cx = 0, cy = 0, cz = 0;
      for (var b = 0; b < numBodies; b++) {
        var theta = (n * j * state.freqs[b] + state.phases[b]) % (2 * Math.PI);
        var phi   = (n * j / state.freqs[b] + state.phases[b]) % Math.PI;
        cx += Math.sin(phi) * Math.cos(theta);
        cy += Math.sin(phi) * Math.sin(theta);
        cz += Math.cos(phi);
      }
      var coherence = Math.sqrt(cx * cx + cy * cy + cz * cz) / numBodies;
      var retVal = {
        draw:  coherence > 0.72,
        turn:  coherence < 0.28,
        turnZ: false
      };
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [
      { id: "or3DFreq",   label: "Freq",   defaultVal: 0.15 },
      { id: "or3DBodies", label: "Bodies", defaultVal: 3 }
    ]
  },
  {
    id: "standingWaves",
    name: "Standing Waves",
    description: "Three sinusoidal oscillators run at frequencies 1, φ, and φ² (golden-ratio multiples). Their amplitudes are summed; dots appear when the combined amplitude exceeds the threshold (constructive interference); the path turns when it falls below the negative threshold (destructive). The beat between any two frequencies is irrational, so the interference envelope never exactly repeats — it creates quasi-periodic density rings in the spiral. Try small Freq values (0.05–0.12) to see wide, distinct rings; larger values produce tighter, more intricate patterns.",
    stateObj: {},
    checker: function(state, n, j, k) {
      var phi = 1.6180339887;
      var s = Math.sin(n * j) + Math.sin(n * j * phi) + Math.sin(n * j * phi * phi);
      return {
        draw: s > k,
        turn: s < -k
      };
    },
    extraVars: [
      { id: "swFreq",      label: "Freq",      defaultVal: 0.07 },
      { id: "swThreshold", label: "Threshold", defaultVal: 1.4 }
    ]
  },
  {
    id: "standingWaves3D",
    name: "Standing Waves 3D",
    description: "Standing Waves extended into 3D: turns in both θ and φ at destructive interference events. Enable 3D Orbit to see how the interference nodes stack into layered 3D shells.",
    is3D: true,
    stateObj: {},
    checker: function(state, n, j, k) {
      var phi = 1.6180339887;
      var s = Math.sin(n * j) + Math.sin(n * j * phi) + Math.sin(n * j * phi * phi);
      var retVal = {
        draw:  s > k,
        turn:  s < -k,
        turnZ: false
      };
      retVal.turnZ = retVal.turn;
      return retVal;
    },
    extraVars: [
      { id: "sw3DFreq",      label: "Freq",      defaultVal: 0.07 },
      { id: "sw3DThreshold", label: "Threshold", defaultVal: 1.4 }
    ]
  },{
    id: "primeGap3D",
    name: "Prime Gap 3D",
    description: "Draws on primes. Turns in 2D on every prime and turns in Z when the gap from the previous prime is at least x.",
    is3D: true,
    stateObj: {
      lastPrime: 2
    },
    checker: function(state, n, x) {
      function isPrime(v) {
        if (v < 2) return false;
        if (v % 2 === 0) return v === 2;
        for (var i = 3; i * i <= v; i += 2) {
          if (v % i === 0) return false;
        }
        return true;
      }

      var retVal = { draw: false, turn: false, turnZ: false };

      if (isPrime(n)) {
        var gap = n - state.lastPrime;
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = gap >= x;
        state.lastPrime = n;
      }

      return retVal;
    },
    extraVars: [
      { id: "x", label: "Z turn when prime gap >=", defaultVal: 6 }
    ]
  },

  {
    id: "fibonacci3D",
    name: "Fibonacci 3D",
    description: "Draws on Fibonacci numbers. Turns in Z when the Fibonacci index is a multiple of x.",
    is3D: true,
    stateObj: {
      a: 1,
      b: 1,
      index: 1
    },
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };

      if (n === state.a) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.index % x === 0;

        var next = state.a + state.b;
        state.a = state.b;
        state.b = next;
        state.index++;
      }

      return retVal;
    },
    extraVars: [
      { id: "x", label: "Z turn every nth Fibonacci hit", defaultVal: 3 }
    ]
  },

  {
    id: "recaman3D",
    name: "Recamán 3D",
    description: "Draws when n appears in the Recamán sequence. Z turn occurs when the sequence jumps forward.",
    is3D: true,
    stateObj: {
      step: 1,
      value: 0,
      seen: {},
      hits: {}
    },
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };

      while (state.step <= n * x) {
        var candidate = state.value - state.step;
        if (candidate > 0 && !state.seen[candidate]) {
          state.value = candidate;
        } else {
          state.value = state.value + state.step;
          state.hits[state.value] = "forward";
        }

        state.seen[state.value] = true;
        state.step++;
      }

      if (state.seen[n]) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.hits[n] === "forward";
      }

      return retVal;
    },
    extraVars: [
      { id: "x", label: "Sequence lookahead multiplier", defaultVal: 2 }
    ]
  },

  {
    id: "divisorRecords3D",
    name: "Divisor Records 3D",
    description: "Draws when n has more divisors than any previous number. These are highly-composite-style record points.",
    is3D: true,
    stateObj: {
      maxDivisors: 0
    },
    checker: function(state, n, x) {
      function divisorCount(v) {
        var count = 0;
        for (var i = 1; i * i <= v; i++) {
          if (v % i === 0) count += i * i === v ? 1 : 2;
        }
        return count;
      }

      var retVal = { draw: false, turn: false, turnZ: false };
      var d = divisorCount(n);

      if (d > state.maxDivisors) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = d >= x;
        state.maxDivisors = d;
      }

      return retVal;
    },
    extraVars: [
      { id: "x", label: "Z turn when divisor count >=", defaultVal: 24 }
    ]
  },

  {
    id: "digitalRoot3D",
    name: "Digital Root 3D",
    description: "Draws when n has the selected digital root. Z turns on every xth hit.",
    is3D: true,
    stateObj: {
      hits: 0
    },
    checker: function(state, n, root, zEvery) {
      function digitalRoot(v) {
        return 1 + ((v - 1) % 9);
      }

      var retVal = { draw: false, turn: false, turnZ: false };

      if (digitalRoot(n) === root) {
        state.hits++;
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.hits % zEvery === 0;
      }

      return retVal;
    },
    extraVars: [
      { id: "root", label: "Digital root", defaultVal: 7 },
      { id: "zEvery", label: "Z turn every nth hit", defaultVal: 3 }
    ]
  },

  {
    id: "collatzRecords3D",
    name: "Collatz Records 3D",
    description: "Draws when n sets a new record for Collatz stopping time.",
    is3D: true,
    stateObj: {
      maxDepth: 0
    },
    checker: function(state, n, x) {
      function collatzDepth(v) {
        var depth = 0;
        while (v !== 1 && depth < 10000) {
          v = v % 2 === 0 ? v / 2 : 3 * v + 1;
          depth++;
        }
        return depth;
      }

      var retVal = { draw: false, turn: false, turnZ: false };
      var depth = collatzDepth(n);

      if (depth > state.maxDepth) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = depth % x === 0;
        state.maxDepth = depth;
      }

      return retVal;
    },
    extraVars: [
      { id: "x", label: "Z turn when depth multiple of", defaultVal: 5 }
    ]
  },

  {
    id: "logisticChaos3D",
    name: "Logistic Chaos 3D",
    description: "Uses a chaotic logistic map. Draws when the internal value rises above a threshold.",
    is3D: true,
    stateObj: {
      value: 0.431
    },
    checker: function(state, n, threshold, zThreshold) {
      var retVal = { draw: false, turn: false, turnZ: false };

      state.value = 3.999 * state.value * (1 - state.value);

      if (state.value > threshold / 100) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.value > zThreshold / 100;
      }

      return retVal;
    },
    extraVars: [
      { id: "threshold", label: "Draw threshold percent", defaultVal: 92 },
      { id: "zThreshold", label: "Z threshold percent", defaultVal: 97 }
    ]
  },

  {
    id: "rule30Stream3D",
    name: "Rule 30 Stream 3D",
    description: "Consumes bits from a hidden Rule 30 cellular automaton. Draws on 1 bits.",
    is3D: true,
    stateObj: {
      row: [1],
      index: 0,
      generation: 0
    },
    checker: function(state, n, zEvery) {
      function nextRow(row) {
        var out = [];
        for (var i = -1; i <= row.length; i++) {
          var left = row[i - 1] || 0;
          var center = row[i] || 0;
          var right = row[i + 1] || 0;
          out.push(left ^ (center || right));
        }
        return out;
      }

      if (state.index >= state.row.length) {
        state.row = nextRow(state.row);
        state.index = 0;
        state.generation++;
      }

      var bit = state.row[state.index++];
      var retVal = { draw: false, turn: false, turnZ: false };

      if (bit === 1) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.generation % zEvery === 0;
      }

      return retVal;
    },
    extraVars: [
      { id: "zEvery", label: "Z turn every nth generation", defaultVal: 4 }
    ]
  },

  {
    id: "primeFactorSignature3D",
    name: "Prime Factor Signature 3D",
    description: "Draws when n has exactly x total prime factors, counting repeated factors.",
    is3D: true,
    stateObj: {},
    checker: function(state, n, x) {
      function factorCount(v) {
        var count = 0;
        var d = 2;
        while (v > 1) {
          while (v % d === 0) {
            count++;
            v = v / d;
          }
          d++;
          if (d * d > v && v > 1) {
            count++;
            break;
          }
        }
        return count;
      }

      var retVal = { draw: false, turn: false, turnZ: false };
      var count = factorCount(n);

      if (count === x) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = n % 2 === 1;
      }

      return retVal;
    },
    extraVars: [
      { id: "x", label: "Total prime factors", defaultVal: 3 }
    ]
  },

  {
    id: "binaryPattern3D",
    name: "Binary Pattern 3D",
    description: "Draws when the binary representation of n contains a configurable pattern.",
    is3D: true,
    stateObj: {
      hits: 0
    },
    checker: function(state, n, pattern, zEvery) {
      var retVal = { draw: false, turn: false, turnZ: false };
      var binary = n.toString(2);
      var pat = String(pattern);

      if (binary.indexOf(pat) !== -1) {
        state.hits++;
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.hits % zEvery === 0;
      }

      return retVal;
    },
    extraVars: [
      { id: "pattern", label: "Binary pattern", defaultVal: 10101 },
      { id: "zEvery", label: "Z turn every nth hit", defaultVal: 5 }
    ]
  },

  {
    id: "binaryPalindrome3D",
    name: "Binary Palindrome 3D",
    description: "Draws when n is a palindrome in binary.",
    is3D: true,
    stateObj: {
      hits: 0
    },
    checker: function(state, n, zEvery) {
      var retVal = { draw: false, turn: false, turnZ: false };
      var b = n.toString(2);
      var r = b.split("").reverse().join("");

      if (b === r) {
        state.hits++;
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.hits % zEvery === 0;
      }

      return retVal;
    },
    extraVars: [
      { id: "zEvery", label: "Z turn every nth palindrome", defaultVal: 2 }
    ]
  },

  {
    id: "rhythmInterference3D",
    name: "Rhythm Interference 3D",
    description: "Combines rhythmic cycles. Draws when n lands on any selected cycle and Z turns when cycles overlap.",
    is3D: true,
    stateObj: {},
    checker: function(state, n, a, b, c) {
      var hitA = n % a === 0;
      var hitB = n % b === 0;
      var hitC = n % c === 0;
      var hits = (hitA ? 1 : 0) + (hitB ? 1 : 0) + (hitC ? 1 : 0);

      return {
        draw: hits > 0,
        turn: hits > 0,
        turnZ: hits >= 2
      };
    },
    extraVars: [
      { id: "a", label: "Cycle A", defaultVal: 3 },
      { id: "b", label: "Cycle B", defaultVal: 5 },
      { id: "c", label: "Cycle C", defaultVal: 7 }
    ]
  },

  {
    id: "memoryGap3D",
    name: "Memory Gap 3D",
    description: "Remembers previous hits. Draws when the gap since the last hit grows beyond x.",
    is3D: true,
    stateObj: {
      lastHit: 0,
      maxGap: 0
    },
    checker: function(state, n, x) {
      var retVal = { draw: false, turn: false, turnZ: false };
      var gap = n - state.lastHit;

      if (gap > state.maxGap && gap >= x) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = gap % 2 === 1;
        state.maxGap = gap;
        state.lastHit = n;
      }

      return retVal;
    },
    extraVars: [
      { id: "x", label: "Minimum gap", defaultVal: 8 }
    ]
  },

  {
    id: "selfSimilarMidpoints3D",
    name: "Self-Similar Midpoints 3D",
    description: "Draws at recursively generated interval midpoints, creating a Cantor-like hierarchy.",
    is3D: true,
    stateObj: {
      initialized: false,
      hits: {}
    },
    checker: function(state, n, maxN, depth) {
      function addMidpoints(lo, hi, d) {
        if (d <= 0 || hi <= lo) return;
        var mid = Math.floor((lo + hi) / 2);
        state.hits[mid] = d;
        addMidpoints(lo, mid - 1, d - 1);
        addMidpoints(mid + 1, hi, d - 1);
      }

      if (!state.initialized) {
        addMidpoints(1, maxN, depth);
        state.initialized = true;
      }

      var retVal = { draw: false, turn: false, turnZ: false };

      if (state.hits[n]) {
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.hits[n] % 2 === 0;
      }

      return retVal;
    },
    extraVars: [
      { id: "maxN", label: "Maximum iteration estimate", defaultVal: 80000 },
      { id: "depth", label: "Recursive depth", defaultVal: 14 }
    ]
  },

  {
    id: "cosmicCoincidence3D",
    name: "Cosmic Coincidence 3D",
    description: "Draws only when several unrelated properties align: prime, digital root, and Collatz parity.",
    is3D: true,
    stateObj: {},
    checker: function(state, n, root) {
      function isPrime(v) {
        if (v < 2) return false;
        if (v % 2 === 0) return v === 2;
        for (var i = 3; i * i <= v; i += 2) {
          if (v % i === 0) return false;
        }
        return true;
      }

      function digitalRoot(v) {
        return 1 + ((v - 1) % 9);
      }

      function collatzDepth(v) {
        var depth = 0;
        while (v !== 1 && depth < 10000) {
          v = v % 2 === 0 ? v / 2 : 3 * v + 1;
          depth++;
        }
        return depth;
      }

      var hit = isPrime(n) &&
        digitalRoot(n) === root &&
        collatzDepth(n) % 2 === 1;

      return {
        draw: hit,
        turn: hit,
        turnZ: hit
      };
    },
    extraVars: [
      { id: "root", label: "Digital root", defaultVal: 7 }
    ]
  },

  {
    id: "constellationDiscovery3D",
    name: "Constellation Discovery 3D",
    description: "Self-referential checker. Draws when the distance to the previous hit is a new distance.",
    is3D: true,
    stateObj: {
      lastHit: 1,
      seenGaps: {},
      initialized: false
    },
    checker: function(state, n, minGap) {
      var retVal = { draw: false, turn: false, turnZ: false };

      if (!state.initialized) {
        state.initialized = true;
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = true;
        return retVal;
      }

      var gap = n - state.lastHit;

      if (gap >= minGap && !state.seenGaps[gap]) {
        state.seenGaps[gap] = true;
        state.lastHit = n;
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = gap % 3 === 0;
      }

      return retVal;
    },
    extraVars: [
      { id: "minGap", label: "Minimum new gap", defaultVal: 2 }
    ]
  },

  {
    id: "squarefree3D",
    name: "Squarefree 3D",
    description: "Draws when n is squarefree. Z turns when n also has at least x distinct prime factors.",
    is3D: true,
    stateObj: {},
    checker: function(state, n, x) {
      var v = n;
      var distinct = 0;
      var squarefree = true;

      for (var d = 2; d * d <= v; d++) {
        var count = 0;
        while (v % d === 0) {
          count++;
          v = v / d;
        }
        if (count > 0) distinct++;
        if (count > 1) squarefree = false;
      }

      if (v > 1) distinct++;

      return {
        draw: squarefree,
        turn: squarefree,
        turnZ: squarefree && distinct >= x
      };
    },
    extraVars: [
      { id: "x", label: "Z turn when distinct factors >=", defaultVal: 3 }
    ]
  },

  {
    id: "lookAndSay3D",
    name: "Look-and-Say 3D",
    description: "Consumes digits from a growing look-and-say sequence. Draws when the current digit is at least x.",
    is3D: true,
    stateObj: {
      sequence: "1",
      index: 0,
      generation: 1
    },
    checker: function(state, n, x, zDigit) {
      function nextLookAndSay(s) {
        var out = "";
        var count = 1;

        for (var i = 1; i <= s.length; i++) {
          if (s[i] === s[i - 1]) {
            count++;
          } else {
            out += String(count) + s[i - 1];
            count = 1;
          }
        }

        return out;
      }

      if (state.index >= state.sequence.length) {
        state.sequence = nextLookAndSay(state.sequence);
        state.index = 0;
        state.generation++;
      }

      var digit = parseInt(state.sequence[state.index++], 10);
      var hit = digit >= x;

      return {
        draw: hit,
        turn: hit,
        turnZ: hit && digit >= zDigit
      };
    },
    extraVars: [
      { id: "x", label: "Draw when digit >=", defaultVal: 2 },
      { id: "zDigit", label: "Z turn when digit >=", defaultVal: 3 }
    ]
  },

  {
    id: "piDigitWalk3D",
    name: "Pi Digit Walk 3D",
    description: "Uses a fixed stream of pi digits as step sizes. Draws when n reaches the next digit-generated target.",
    is3D: true,
    stateObj: {
      digits: "3141592653589793238462643383279502884197169399375105820974944592",
      index: 0,
      next: 3,
      hits: 0
    },
    checker: function(state, n, zEvery) {
      var retVal = { draw: false, turn: false, turnZ: false };

      if (n === state.next) {
        state.hits++;
        retVal.draw = true;
        retVal.turn = true;
        retVal.turnZ = state.hits % zEvery === 0;

        var digit = parseInt(state.digits[state.index % state.digits.length], 10);
        state.index++;
        state.next += Math.max(1, digit);
      }

      return retVal;
    },
    extraVars: [
      { id: "zEvery", label: "Z turn every nth pi hit", defaultVal: 4 }
    ]
  },

  {
    id: "mandelbrotLine3D",
    name: "Mandelbrot Line 3D",
    description: "Maps n across a line in the Mandelbrot plane. Draws when escape time is high.",
    is3D: true,
    stateObj: {},
    checker: function(state, n, threshold, scale) {
      var cx = -2.0 + (n % scale) * (3.0 / scale);
      var cy = -0.65 + (Math.floor(n / scale) % scale) * (1.3 / scale);

      var zx = 0;
      var zy = 0;
      var iter = 0;
      var maxIter = 40;

      while (zx * zx + zy * zy <= 4 && iter < maxIter) {
        var xt = zx * zx - zy * zy + cx;
        zy = 2 * zx * zy + cy;
        zx = xt;
        iter++;
      }

      var hit = iter >= threshold;

      return {
        draw: hit,
        turn: hit,
        turnZ: iter === maxIter
      };
    },
    extraVars: [
      { id: "threshold", label: "Escape threshold", defaultVal: 20 },
      { id: "scale", label: "Plane scan width", defaultVal: 300 }
    ]
  },

  {
    id: "stateMood3D",
    name: "State Mood 3D",
    description: "Maintains an internal energy level. Primes increase energy, composites decrease it. Draws while energized.",
    is3D: true,
    stateObj: {
      energy: 0
    },
    checker: function(state, n, threshold, gain, decay) {
      function isPrime(v) {
        if (v < 2) return false;
        if (v % 2 === 0) return v === 2;
        for (var i = 3; i * i <= v; i += 2) {
          if (v % i === 0) return false;
        }
        return true;
      }

      if (isPrime(n)) {
        state.energy += gain;
      } else {
        state.energy -= decay;
      }

      if (state.energy < 0) state.energy = 0;
      if (state.energy > 100) state.energy = 100;

      var hit = state.energy >= threshold;

      return {
        draw: hit,
        turn: hit,
        turnZ: hit && state.energy > threshold + 20
      };
    },
    extraVars: [
      { id: "threshold", label: "Energy draw threshold", defaultVal: 40 },
      { id: "gain", label: "Prime energy gain", defaultVal: 7 },
      { id: "decay", label: "Composite energy decay", defaultVal: 1 }
    ]
  },

  {
    id: "sequenceComposer3D",
    name: "Sequence Composer 3D",
    description: "Combines prime, Fibonacci, and digital-root rules with XOR-like behavior.",
    is3D: true,
    stateObj: {
      fibA: 1,
      fibB: 1
    },
    checker: function(state, n, root) {
      function isPrime(v) {
        if (v < 2) return false;
        if (v % 2 === 0) return v === 2;
        for (var i = 3; i * i <= v; i += 2) {
          if (v % i === 0) return false;
        }
        return true;
      }

      function digitalRoot(v) {
        return 1 + ((v - 1) % 9);
      }

      var fibHit = false;
      if (n === state.fibA) {
        fibHit = true;
        var next = state.fibA + state.fibB;
        state.fibA = state.fibB;
        state.fibB = next;
      }

      var primeHit = isPrime(n);
      var rootHit = digitalRoot(n) === root;
      var count = (primeHit ? 1 : 0) + (fibHit ? 1 : 0) + (rootHit ? 1 : 0);
      var hit = count === 1 || count === 3;

      return {
        draw: hit,
        turn: hit,
        turnZ: count >= 2
      };
    },
    extraVars: [
      { id: "root", label: "Digital root", defaultVal: 7 }
    ]
  }
];

