let checkers = [
  {
    id : "pi",
    name : "Pi Digits",
    description : "After iterating (the next digit of pi) times, plot a point and turn",
    stateObj : {
      idx: 0, next: 3, piidx: 0
    },
    checker : function(state, n ) {
      var retVal = false;
      if( state.idx == state.next ) {
        state.idx = 0;
        state.next = parseInt( piString.charAt( state.piidx++ ) );
        retVal = true;
      } else {
        state.idx++;
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
      var retVal = false;
      var val = parseInt( piString.charAt( n ) );
      if( val == x ) {
        retVal = true;
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
      var retVal = false;
      var val = parseInt( piString.charAt( n ) );
      if( val == 2 || val == 3 || val == 5 || val == 7 ) {
        retVal = true;
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
      return Math.random() <= v;
    },
    extraVars : [
      {
        id : "v",
        label : "Random Cutoff (0 <= x < 1)"
      }
    ]
  }
];