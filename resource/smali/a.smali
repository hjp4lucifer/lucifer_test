.class public Lcom/kingreader/framework/a/a/a/a;
.super Ljava/lang/Object;

# interfaces
.implements Lcom/kingreader/framework/a/a/a/c;


# instance fields
.field private a:Ljava/nio/charset/CharsetDecoder;

.field private b:Ljava/nio/CharBuffer;

.field private c:Ljava/nio/ByteBuffer;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x1

    invoke-static {v0}, Ljava/nio/CharBuffer;->allocate(I)Ljava/nio/CharBuffer;

    move-result-object v0

    iput-object v0, p0, Lcom/kingreader/framework/a/a/a/a;->b:Ljava/nio/CharBuffer;

    const/4 v0, 0x2

    invoke-static {v0}, Ljava/nio/ByteBuffer;->allocate(I)Ljava/nio/ByteBuffer;

    move-result-object v0

    iput-object v0, p0, Lcom/kingreader/framework/a/a/a/a;->c:Ljava/nio/ByteBuffer;

    invoke-static {}, Ljava/nio/charset/Charset;->defaultCharset()Ljava/nio/charset/Charset;

    move-result-object v0

    invoke-virtual {v0}, Ljava/nio/charset/Charset;->newDecoder()Ljava/nio/charset/CharsetDecoder;

    move-result-object v0

    iput-object v0, p0, Lcom/kingreader/framework/a/a/a/a;->a:Ljava/nio/charset/CharsetDecoder;

    return-void
.end method

.method public constructor <init>(Ljava/lang/String;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x1

    invoke-static {v0}, Ljava/nio/CharBuffer;->allocate(I)Ljava/nio/CharBuffer;

    move-result-object v0

    iput-object v0, p0, Lcom/kingreader/framework/a/a/a/a;->b:Ljava/nio/CharBuffer;

    const/4 v0, 0x2

    invoke-static {v0}, Ljava/nio/ByteBuffer;->allocate(I)Ljava/nio/ByteBuffer;

    move-result-object v0

    iput-object v0, p0, Lcom/kingreader/framework/a/a/a/a;->c:Ljava/nio/ByteBuffer;

    if-eqz p1, :cond_0

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v0

    if-lez v0, :cond_0

    invoke-static {p1}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object v0

    invoke-virtual {v0}, Ljava/nio/charset/Charset;->newDecoder()Ljava/nio/charset/CharsetDecoder;

    move-result-object v0

    iput-object v0, p0, Lcom/kingreader/framework/a/a/a/a;->a:Ljava/nio/charset/CharsetDecoder;

    :goto_0
    return-void

    :cond_0
    invoke-static {}, Ljava/nio/charset/Charset;->defaultCharset()Ljava/nio/charset/Charset;

    move-result-object v0

    invoke-virtual {v0}, Ljava/nio/charset/Charset;->newDecoder()Ljava/nio/charset/CharsetDecoder;

    move-result-object v0

    iput-object v0, p0, Lcom/kingreader/framework/a/a/a/a;->a:Ljava/nio/charset/CharsetDecoder;

    goto :goto_0
.end method


# virtual methods
.method public a()B
    .locals 1

    const/4 v0, 0x0

    return v0
.end method

.method public a(Lcom/kingreader/framework/a/a/a;)Z
    .locals 7

    const/4 v3, 0x2

    const/4 v6, 0x0

    const/4 v5, 0x1

    iget v0, p1, Lcom/kingreader/framework/a/a/a;->b:I

    add-int/lit8 v0, v0, 0x1

    iget-object v1, p1, Lcom/kingreader/framework/a/a/a;->a:[B

    array-length v1, v1

    if-lt v0, v1, :cond_0

    move v0, v6

    :goto_0
    return v0

    :cond_0
    iget-object v0, p1, Lcom/kingreader/framework/a/a/a;->a:[B

    iget v1, p1, Lcom/kingreader/framework/a/a/a;->b:I

    aget-byte v0, v0, v1

    and-int/lit16 v0, v0, 0xff

    int-to-char v0, v0

    const/16 v1, 0x80

    if-ge v0, v1, :cond_1

    iget v1, p1, Lcom/kingreader/framework/a/a/a;->b:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p1, Lcom/kingreader/framework/a/a/a;->c:I

    iput-char v0, p1, Lcom/kingreader/framework/a/a/a;->d:C

    move v0, v5

    goto :goto_0

    :cond_1
    iget-object v1, p1, Lcom/kingreader/framework/a/a/a;->a:[B

    array-length v1, v1

    iget v2, p1, Lcom/kingreader/framework/a/a/a;->b:I

    sub-int/2addr v1, v2

    if-le v1, v3, :cond_2

    move v1, v3

    :goto_1
    iget-object v2, p0, Lcom/kingreader/framework/a/a/a/a;->c:Ljava/nio/ByteBuffer;

    invoke-virtual {v2}, Ljava/nio/ByteBuffer;->rewind()Ljava/nio/Buffer;

    iget-object v2, p0, Lcom/kingreader/framework/a/a/a/a;->c:Ljava/nio/ByteBuffer;

    iget-object v3, p1, Lcom/kingreader/framework/a/a/a;->a:[B

    iget v4, p1, Lcom/kingreader/framework/a/a/a;->b:I

    invoke-virtual {v2, v3, v4, v1}, Ljava/nio/ByteBuffer;->put([BII)Ljava/nio/ByteBuffer;

    iget-object v1, p0, Lcom/kingreader/framework/a/a/a/a;->c:Ljava/nio/ByteBuffer;

    invoke-virtual {v1}, Ljava/nio/ByteBuffer;->rewind()Ljava/nio/Buffer;

    iget-object v1, p0, Lcom/kingreader/framework/a/a/a/a;->b:Ljava/nio/CharBuffer;

    invoke-virtual {v1}, Ljava/nio/CharBuffer;->rewind()Ljava/nio/Buffer;

    iget-object v1, p0, Lcom/kingreader/framework/a/a/a/a;->a:Ljava/nio/charset/CharsetDecoder;

    iget-object v2, p0, Lcom/kingreader/framework/a/a/a/a;->c:Ljava/nio/ByteBuffer;

    iget-object v3, p0, Lcom/kingreader/framework/a/a/a/a;->b:Ljava/nio/CharBuffer;

    invoke-virtual {v1, v2, v3, v5}, Ljava/nio/charset/CharsetDecoder;->decode(Ljava/nio/ByteBuffer;Ljava/nio/CharBuffer;Z)Ljava/nio/charset/CoderResult;

    move-result-object v1

    invoke-virtual {v1}, Ljava/nio/charset/CoderResult;->isError()Z

    move-result v2

    if-eqz v2, :cond_3

    const/16 v0, 0x20

    iput-char v0, p1, Lcom/kingreader/framework/a/a/a;->d:C

    iget v0, p1, Lcom/kingreader/framework/a/a/a;->b:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p1, Lcom/kingreader/framework/a/a/a;->c:I

    :goto_2
    move v0, v5

    goto :goto_0

    :cond_2
    iget-object v1, p1, Lcom/kingreader/framework/a/a/a;->a:[B

    array-length v1, v1

    iget v2, p1, Lcom/kingreader/framework/a/a/a;->b:I

    sub-int/2addr v1, v2

    goto :goto_1

    :cond_3
    invoke-virtual {v1}, Ljava/nio/charset/CoderResult;->isOverflow()Z

    move-result v1

    if-eqz v1, :cond_4

    iget v1, p1, Lcom/kingreader/framework/a/a/a;->b:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p1, Lcom/kingreader/framework/a/a/a;->c:I

    iput-char v0, p1, Lcom/kingreader/framework/a/a/a;->d:C

    goto :goto_2

    :cond_4
    iget v0, p1, Lcom/kingreader/framework/a/a/a;->b:I

    add-int/lit8 v0, v0, 0x2

    iput v0, p1, Lcom/kingreader/framework/a/a/a;->c:I

    iget-object v0, p0, Lcom/kingreader/framework/a/a/a/a;->b:Ljava/nio/CharBuffer;

    invoke-virtual {v0, v6}, Ljava/nio/CharBuffer;->get(I)C

    move-result v0

    iput-char v0, p1, Lcom/kingreader/framework/a/a/a;->d:C

    goto :goto_2
.end method

.method public b()B
    .locals 1

    const/4 v0, 0x2

    return v0
.end method

.method public b(Lcom/kingreader/framework/a/a/a;)Z
    .locals 7

    const/4 v6, 0x0

    const/4 v5, 0x2

    const/4 v4, 0x1

    if-eqz p1, :cond_0

    iget-object v0, p1, Lcom/kingreader/framework/a/a/a;->a:[B

    if-eqz v0, :cond_0

    iget v0, p1, Lcom/kingreader/framework/a/a/a;->b:I

    if-ge v0, v4, :cond_1

    :cond_0
    move v0, v6

    :goto_0
    return v0

    :cond_1
    iget-object v0, p1, Lcom/kingreader/framework/a/a/a;->a:[B

    iget v1, p1, Lcom/kingreader/framework/a/a/a;->b:I

    sub-int/2addr v1, v4

    aget-byte v0, v0, v1

    and-int/lit16 v0, v0, 0xff

    int-to-char v0, v0

    iget v1, p1, Lcom/kingreader/framework/a/a/a;->b:I

    if-ne v1, v4, :cond_2

    iput-char v0, p1, Lcom/kingreader/framework/a/a/a;->d:C

    iput v6, p1, Lcom/kingreader/framework/a/a/a;->c:I

    move v0, v4

    goto :goto_0

    :cond_2
    iget-object v1, p1, Lcom/kingreader/framework/a/a/a;->a:[B

    iget v2, p1, Lcom/kingreader/framework/a/a/a;->b:I

    aget-byte v1, v1, v2

    and-int/lit16 v1, v1, 0xff

    int-to-char v1, v1

    iget-object v2, p1, Lcom/kingreader/framework/a/a/a;->a:[B

    iget v3, p1, Lcom/kingreader/framework/a/a/a;->b:I

    sub-int/2addr v3, v5

    aget-byte v2, v2, v3

    and-int/lit16 v2, v2, 0xff

    int-to-char v2, v2

    invoke-static {v0}, Lcom/kingreader/framework/a/a/a/d;->a(C)Z

    move-result v3

    if-eqz v3, :cond_4

    invoke-static {v2}, Lcom/kingreader/framework/a/a/a/d;->a(C)Z

    move-result v2

    if-nez v2, :cond_3

    invoke-static {v1}, Lcom/kingreader/framework/a/a/a/d;->a(C)Z

    move-result v1

    if-eqz v1, :cond_4

    :cond_3
    iput-char v0, p1, Lcom/kingreader/framework/a/a/a;->d:C

    iget v0, p1, Lcom/kingreader/framework/a/a/a;->b:I

    sub-int/2addr v0, v4

    iput v0, p1, Lcom/kingreader/framework/a/a/a;->c:I

    move v0, v4

    goto :goto_0

    :cond_4
    iget-object v1, p0, Lcom/kingreader/framework/a/a/a/a;->c:Ljava/nio/ByteBuffer;

    invoke-virtual {v1}, Ljava/nio/ByteBuffer;->rewind()Ljava/nio/Buffer;

    iget-object v1, p0, Lcom/kingreader/framework/a/a/a/a;->c:Ljava/nio/ByteBuffer;

    iget-object v2, p1, Lcom/kingreader/framework/a/a/a;->a:[B

    iget v3, p1, Lcom/kingreader/framework/a/a/a;->b:I

    sub-int/2addr v3, v5

    invoke-virtual {v1, v2, v3, v5}, Ljava/nio/ByteBuffer;->put([BII)Ljava/nio/ByteBuffer;

    iget-object v1, p0, Lcom/kingreader/framework/a/a/a/a;->c:Ljava/nio/ByteBuffer;

    invoke-virtual {v1}, Ljava/nio/ByteBuffer;->rewind()Ljava/nio/Buffer;

    iget-object v1, p0, Lcom/kingreader/framework/a/a/a/a;->b:Ljava/nio/CharBuffer;

    invoke-virtual {v1}, Ljava/nio/CharBuffer;->rewind()Ljava/nio/Buffer;

    iget-object v1, p0, Lcom/kingreader/framework/a/a/a/a;->a:Ljava/nio/charset/CharsetDecoder;

    iget-object v2, p0, Lcom/kingreader/framework/a/a/a/a;->c:Ljava/nio/ByteBuffer;

    iget-object v3, p0, Lcom/kingreader/framework/a/a/a/a;->b:Ljava/nio/CharBuffer;

    invoke-virtual {v1, v2, v3, v4}, Ljava/nio/charset/CharsetDecoder;->decode(Ljava/nio/ByteBuffer;Ljava/nio/CharBuffer;Z)Ljava/nio/charset/CoderResult;

    move-result-object v1

    invoke-virtual {v1}, Ljava/nio/charset/CoderResult;->isError()Z

    move-result v2

    if-eqz v2, :cond_5

    iget v1, p1, Lcom/kingreader/framework/a/a/a;->b:I

    sub-int/2addr v1, v4

    iput v1, p1, Lcom/kingreader/framework/a/a/a;->c:I

    iput-char v0, p1, Lcom/kingreader/framework/a/a/a;->d:C

    :goto_1
    move v0, v4

    goto :goto_0

    :cond_5
    invoke-virtual {v1}, Ljava/nio/charset/CoderResult;->isOverflow()Z

    move-result v1

    if-eqz v1, :cond_6

    iget v1, p1, Lcom/kingreader/framework/a/a/a;->b:I

    sub-int/2addr v1, v4

    iput v1, p1, Lcom/kingreader/framework/a/a/a;->c:I

    iput-char v0, p1, Lcom/kingreader/framework/a/a/a;->d:C

    goto :goto_1

    :cond_6
    iget v0, p1, Lcom/kingreader/framework/a/a/a;->b:I

    sub-int/2addr v0, v5

    iput v0, p1, Lcom/kingreader/framework/a/a/a;->c:I

    iget-object v0, p0, Lcom/kingreader/framework/a/a/a/a;->b:Ljava/nio/CharBuffer;

    invoke-virtual {v0}, Ljava/nio/CharBuffer;->clear()Ljava/nio/Buffer;

    iget-object v0, p0, Lcom/kingreader/framework/a/a/a/a;->b:Ljava/nio/CharBuffer;

    invoke-virtual {v0, v6}, Ljava/nio/CharBuffer;->get(I)C

    move-result v0

    iput-char v0, p1, Lcom/kingreader/framework/a/a/a;->d:C

    goto :goto_1
.end method

.method public c()B
    .locals 1

    const/16 v0, 0xa

    return v0
.end method

.method public d()Z
    .locals 1

    const/4 v0, 0x0

    return v0
.end method
