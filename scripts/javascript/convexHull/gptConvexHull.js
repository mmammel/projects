function getConvexHull(points) {
  // sort points by x-coordinate
  points.sort(function(a, b) {
    if (a.x != b.x) {
      return a.x - b.x;
    } else if (a.y != b.y) {
      return a.y - b.y;
    } else {
      return a.z - b.z;
    }
  });
  
  var n = points.length;
  var stack = [];
  
  // push the first two points onto the stack
  stack.push(points[0]);
  stack.push(points[1]);
  
  // iterate over the remaining points
  for (var i = 2; i < n; i++) {
    // get the current point
    var p = points[i];
    
    // pop points off the stack until the last three points are not a right turn
    while (stack.length >= 3) {
      var top = stack[stack.length - 1];
      var nextToTop = stack[stack.length - 2];
      var thirdToTop = stack[stack.length - 3];
      if (rightTurn(thirdToTop, nextToTop, top, p)) {
        break;
      }
      stack.pop();
    }
    
    // push the current point onto the stack
    stack.push(p);
  }
  
  return stack;
}

function rightTurn(a, b, c, d) {
  // calculate the vectors ab and ac
  var ab = {x: b.x - a.x, y: b.y - a.y, z: b.z - a.z};
  var ac = {x: c.x - a.x, y: c.y - a.y, z: c.z - a.z};
  
  // calculate the normal vector to the plane containing abc
  var normal = {
    x: ab.y * ac.z - ab.z * ac.y,
    y: ab.z * ac.x - ab.x * ac.z,
    z: ab.x * ac.y - ab.y * ac.x
  };
  
  // calculate the dot product of the normal and ad
  var dotProduct = normal.x * d.x + normal.y * d.y + normal.z * d.z;
  
  // return true if the dot product is negative, indicating a right turn
  return dotProduct < 0;
}

