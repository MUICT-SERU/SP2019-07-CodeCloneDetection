import React, {Component} from 'react';
import { Form, Input, Button } from 'antd';


class Content extends Component {
  handleSubmit = e => {
     e.preventDefault();
     this.props.form.validateFields((err, values) => {
       if (!err) {
         console.log('Received values of form: ', values);
       }
     });
   };
  render() {
    const { getFieldDecorator } = this.props.form;
    return (
      <Form align = "center" labelCol={{ span: 10 }} wrapperCol={{ span: 12 }} onSubmit={this.handleSubmit}>
      <label><h3><b>Input Github URL for detection clone</b></h3></label>
        <Form.Item>
          {getFieldDecorator('note', {
            rules: [{ required: true, message: 'Please input Github URL!' }],
          })(<Input placeholder="Input Github URL here"/> )}
        </Form.Item>
        <Form.Item align="center" wrapperCol={{ span: 12, offset: 5 }}>
         <Button type="primary" htmlType="submit">
           Submit
         </Button>
       </Form.Item>
       </Form>
    );
}
}

const WrappedForm = Form.create({ name: 'coordinated' })(Content);
export default WrappedForm;
