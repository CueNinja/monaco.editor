import React from 'react';
import ReactResizeDetector from 'react-resize-detector';

import 'monaco-editor/esm/vs/editor/editor.all.js';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api.js';

import 'monaco-editor/esm/vs/language/typescript/monaco.contribution';
import 'monaco-editor/esm/vs/language/css/monaco.contribution';
import 'monaco-editor/esm/vs/language/json/monaco.contribution';
import 'monaco-editor/esm/vs/language/html/monaco.contribution';
import 'monaco-editor/esm/vs/basic-languages/monaco.contribution.js';

class MonacoEditor extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
          width: window.innerWidth,
          height: window.innerHeight
        };
    }
    onResize(width, height) {
      this.setState({width, height});
      this.editor?.render();
    }
    componentDidMount() {
        if (!!this.props.theme) {
            monaco.editor.defineTheme(this.props.theme.key, this.props.theme);
            monaco.editor.setTheme(this.props.theme.key);
        }
        const editor = monaco.editor.create(
          this.el,
          {
            ...this.props.options,
            value: this.props.content,
            automaticLayout: true
          }
        );
        editor.onDidChangeModelContent((event) => {
          this.props?.onChange?.(editor.getValue(), event)
        });
        this.editor = editor;
    }
    render() {
        this.editor?.setValue(this.props.content)
        this.editor?.setTheme(this.props?.options?.theme || "vs")
        return (
            <div style={{height: '100%', overflow: 'hidden'}}>
                <ReactResizeDetector handleWidth handleHeight onResize={::this.onResize}/>
                <div
                    ref={el => this.el = el}
                    style={{
                        height: this.state?.height,
                        width: this.state?.width,
                        overflow: 'hidden'
                    }}
                />
            </div>
        );
    }
}

export default MonacoEditor;
