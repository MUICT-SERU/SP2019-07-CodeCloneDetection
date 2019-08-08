import React, { Component } from 'react';
import './App.css';
import Header from './header.js';
import { Layout } from 'antd';
import Content from './content.js';
import { BrowserRouter as Router, Route, Link} from "react-router-dom";
import AppLog from './GitLogin';

const { header, footer, content } = Layout;
class App extends Component{
  render(){
    return(
      <div>
          <Layout>
          <AppLog />
            <header><Header /></header>
            <content><Content /></content>
            <footer>Footer</footer>
          </Layout>
          </div>
  );
  }
}

export default App;
