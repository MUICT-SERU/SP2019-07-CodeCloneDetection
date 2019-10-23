import React, { Component } from 'react';
import './App.css';
import Header from './header.js';
import { Layout } from 'antd';
import Content from './content.js';

const { header, footer, content } = Layout;
class App extends Component{
  render(){
    return(
      <div>
          <Layout>
            <header><Header /></header>
            <a herf='https://github.com/login/oauth/authorize?client_id=31ec50555fca84725afa'>github</a>
            <content><Content /></content>
            <footer>Footer</footer>
          </Layout>
          </div>
  );
  }
}

export default App;
