package src;

interface UI {
    void DrawGame();

    void DrawField(CellState[][] cells, boolean userField);

    void SetCallbackListener(CallbackListener listener);

    void ShowMessage(String message);

    void Repaint();
}

interface CallbackListener {
    void onStep(Coord coord);
}
