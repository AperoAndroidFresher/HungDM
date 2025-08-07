package com.example.hungdm.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R
import com.example.hungdm.component.Dropdown
import com.example.hungdm.model.Playlist
import com.example.hungdm.model.Song

@Composable
fun ItemGrid(
    modifier: Modifier = Modifier,
    option1:String="",
    option2:String="",
    icon1: Int = R.drawable.outline_delete_24,
    icon2: Int = R.drawable.outline_share_24,
    song: Song = Song(100,"Noi nay co anh","MTP", duration = 100000L ,null),
    playlist: Playlist? = null,
    showOption: Boolean = false,
    onClickShowOption: () -> Unit = {},
    onClickOption1: () -> Unit = {},
    onClickOption2: ()->Unit = {},
    onDismissRequest: () -> Unit = {}
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.wrapContentSize()
    ) {
        Box() {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data( if(playlist!=null) R.drawable.img1 else song.img)
                    .crossfade(true)
                    .error(R.drawable.img1)
                    .size(300, 300)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(135.dp)
                    .align(Alignment.Center)
            )

            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = onClickShowOption,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color(0xB2000000), CircleShape)
                        .size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.about),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
                Dropdown(
                    expanded = showOption,
                    option1 = option1,
                    option2 = option2,
                    icon1 = icon1,
                    icon2 = icon2,
                    onClickOption1 = onClickOption1,
                    onClickOption2 = onClickOption2,
                    onDismissRequest = onDismissRequest
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            text = playlist?.title ?: song.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            text = playlist?.songNumberStr ?: song.artist,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        if(playlist==null){
            Text(
                text = song.time,
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                color = colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )
        }

    }


}