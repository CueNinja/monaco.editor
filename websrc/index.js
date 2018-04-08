import React from 'react';
import { render } from 'react-dom';
import MonacoEditor from './monaco';

class App extends React.Component {
 constructor(props) {
   super(props);
   const content = window.javafx?.getContent?.()
   const language = window.javafx?.getLanguage?.()
   const theme = window.javafx?.getTheme?.()
   this.state = {
     content: content?.length > 0 ? content : '// type your code...',
     language,
     theme
   };
   window.updateContent = ::this.updateContent
   window.setTheme = ::this.setTheme
 }
 editorDidMount(editor, monaco) {
   editor.focus();
 }
 onChange(newValue, e) {
   window.javafx?.contentChanged(newValue)
 }
 updateContent(content) {
   this.setState({content})
 }
 setTheme(theme) {
   this.setState({theme})
 }
 render() {
   const code = this.state.code;
   const options = {
     selectOnLineNumbers: true
   };
   return (
     <MonacoEditor
       options={{language:this.state?.language, theme:this.state?.theme || "vs"}}
       onChange={::this.onChange}
       content={this.state?.content}
     />
   );
 }
}


render(
 <App />,
 document.getElementById('root')
);
