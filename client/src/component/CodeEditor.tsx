import * as React from 'react';
import AceEditor from 'react-ace';
import 'brace/theme/monokai';

interface EditorData {
  fileType: string;
  title: string;
  contents: string;
}

export type onEditorChange = (value: string) => void;

export const createEditor = (post: EditorData, readOnly: boolean, onChange?: onEditorChange): JSX.Element => {
  try {
    require(`brace/mode/${post.fileType}`);
    console.log(`new mode: ${post.fileType}`);
  } catch (e) {
    console.log('error new mode: ' + e);
  }
  const lines = function() {
    const lineNum = post.contents.split('\n').length;
    return (lineNum < 50) ? lineNum : 50;
  }();
  return (
    <AceEditor
      mode={post.fileType}
      theme="monokai"
      value={post.contents}
      // width="60em"
      // minLines={lines}
      maxLines={lines}
      readOnly={readOnly}
      // focus={false}
      onChange={(text) => { if (onChange) { onChange(text); } else { console.log('onChange undefined' + text); } }}
    />
  );
};