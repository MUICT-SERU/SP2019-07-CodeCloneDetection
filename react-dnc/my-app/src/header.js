import React, { Component } from 'react';

class Header extends Component{
  render(){
    var headstyle={
      fontSize: 64,
      marginBottom:5,
      marginTop:100
    };var subheadstyle={
      fontSize: 36,
      marginBottom: 275
    };
    return(
  <div align = 'center'>
    <h5 style={headstyle}> Code Clone Dection </h5>
    <h5 style={subheadstyle}> Do Not Copy Group </h5>
  </div>
  );
  }
}
export default Header;
