"use client";
import React, { useCallback } from 'react'
import styles from './toolbar.module.scss';
import { Editor } from '@tiptap/react';

type ToolbarProps = {
  editor: Editor;
}

const Toolbar = ({ editor }: ToolbarProps) => {
  const setLink = useCallback(() => {
    const href = prompt("Enter the URL", "https://");
    const text = prompt("Enter the text", "Some Text");

    if (!href || !text) return;

    const { state } = editor;
    const { selection } = state;
    const { from, to } = selection;
    const { $from } = selection;

    const isTextSelected = from < to;
    const nodeAtSelection = $from.node();
    let tr;

    // 드래그 한 후 텍스트 선택 시
    if (
      nodeAtSelection &&
      nodeAtSelection.type.name !== "codeBlock" &&
      isTextSelected
    ) {
      tr = state.tr.deleteSelection();
      tr = state.tr.insertText(text as string);

      const linkMarkType = state.schema.marks.link;
      const linkMark = linkMarkType.create({ href });
      // 새로 넣은 텍스트 시작 위치(from)부터 끝 위치(to)를 링크로 변경
      tr = tr.addMark(from, from + (text as string).length, linkMark);

      editor.view.dispatch(tr);
    } else if (nodeAtSelection.type.name !== "codeBlock") {
      editor
        .chain()
        .focus()
        .setLink({ href })
        .insertContent(text)
        .run();
    }
  }, [editor]);

  return (
    <div className={styles.toolbar}>
      <div className={styles.itemBox}>
        <button
          type="button"
          className={`${styles.toolbarBtn} ${styles.h1} ${
            editor.isActive("heading", { level: 2 }) ? styles.active : styles.none
          }`}
          onClick={() =>
            editor.chain().focus().toggleHeading({ level: 2 }).run()
          }
          disabled={
            !editor.can().chain().focus().toggleHeading({ level: 2 }).run()
          }
        />
        <button
          type="button"
          className={`${styles.toolbarBtn} ${styles.h2} ${
            editor.isActive("heading", { level: 3 }) ? styles.active : styles.none
          }`}
          onClick={() =>
            editor.chain().focus().toggleHeading({ level: 3 }).run()
          }
          disabled={
            !editor.can().chain().focus().toggleHeading({ level: 3 }).run()
          }
        />
      </div>
      <div className={styles.line} />
      <div className={styles.itemBox}>
        <button
          type="button"
          className={`${styles.toolbarBtn} ${styles.bold} ${
            editor.isActive("bold") ? styles.active : styles.none
          }`}
          onClick={() => editor.chain().focus().toggleBold().run()}
          disabled={!editor.can().chain().focus().toggleBold().run()}
        />
        <button
          type="button"
          className={`${styles.toolbarBtn} ${styles.italic} ${
            editor.isActive("italic") ? styles.active : styles.none
          }`}
          onClick={() => editor.chain().focus().toggleItalic().run()}
          disabled={!editor.can().chain().focus().toggleItalic().run()}
        />
        <button
          type="button"
          className={`${styles.toolbarBtn} ${styles.strike} ${
            editor.isActive("strike") ? styles.active : styles.none
          }`}
          onClick={() => editor.chain().focus().toggleStrike().run()}
          disabled={!editor.can().chain().focus().toggleStrike().run()}
        />
      </div>
      <div className={`${styles.line} `} />
      <div className={styles.itemBox}>
        <button
          type="button"
          className={`${styles.toolbarBtn} ${styles.bulleted} ${
            editor.isActive("bulletList") ? styles.active : styles.none
          }`}
          onClick={() => editor.chain().focus().toggleBulletList().run()}
        />
        <button
          type="button"
          className={`${styles.toolbarBtn} ${styles.numbered} ${
            editor.isActive("orderedList") ? styles.active : styles.none
          }`}
          onClick={() => editor.chain().focus().toggleOrderedList().run()}
        />
      </div>
      <div className={styles.line} />
      <div className={styles.itemBox}>
        <button
            type="button"
            className={`${styles.toolbarBtn} ${styles.link} ${
              editor.isActive("link") ? styles.active : styles.none
            }`}
            onClick={setLink}
          />
        <button
          type="button"
          className={`${styles.toolbarBtn} ${styles.newline} ${styles.none}`}
          onClick={() => editor.chain().focus().setHorizontalRule().run()}
        />
      </div>
    </div>
  )
}

export default Toolbar