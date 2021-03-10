/**
 * checkers return an object like this:
 * 
 * {
 *   draw: boolean,
 *   turn: boolean
 * }
 */

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
        label : "Turn if divisible by: "
      }
    ]
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
        label : "Multiple of what?"
      }
    ]
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
        values : [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ]
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
      if( n <= 1 ) {
        retVal.draw = false;
        retVal.turn = false;
      } else if( n == 2 ) {
        retVal.draw = true;
        retVal.turn = true;
      } else if( n % 2 == 0 ) {
        retVal.draw = false;
        retVal.turn = false;
      } else {
        var s = Math.sqrt( n );
        var d = 3;
        while( d <= s ) {
          if( n % d == 0 ) {
            retVal.draw = false;
            retVal.turn = false;
            break;
          }
          d+=2;
        }
      }
      return retVal;
    }
  },
  // {
  //   id: "fib",
  //   name: "Fibonacci",
  //   description: "If the iteration is a fibonacci number, plot a point and turn",
  //   stateObj : {num0: 0, num1: 1, val: 1},
  //   checker : function(state, n) {
  //     var retVal = false;
  //     if( n == state.val ) {
  //       state.num0 = state.num1;
  //       state.num1 = state.val;
  //       state.val = state.num0 + state.num1;
  //       retVal = true;
  //     }
  
  //     return retVal;
  //   }
  // },
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
        label : "Random Cutoff (0 <= v < 1)"
      }
    ]
  }
];

