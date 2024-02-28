"use client"

import { useState } from "react";
import { useEditor, EditorContent } from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";
import Link from "@tiptap/extension-link";
import { Markdown } from "tiptap-markdown";

import styles from "./page.module.scss";
import Toolbar from "./components/toolbar";

import CustomCodeBlockLowlight from "./util/codeBlockIndent";
import { Indent } from "./util/indent";

export default function Home() {
  const [text, setText] = useState("Hello World!");
  const editor = useEditor({
    extensions: [
      StarterKit.configure({
        codeBlock: false,
      }),
      Link.extend({ inclusive: false }).configure({
        openOnClick: false,
      }),
      Markdown,
      CustomCodeBlockLowlight,
      Indent
    ],
    content: text,
    onUpdate({ editor }) {
      setText(editor.getHTML());
    }
  });

  return (
    <main className={styles.main}>
      <div className={styles.editor}>
        { editor && <Toolbar editor={editor} /> }
        <EditorContent editor={editor} />
      </div>
    </main>
  );
}
