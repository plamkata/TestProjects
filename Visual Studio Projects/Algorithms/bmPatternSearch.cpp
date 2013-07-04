#include <stdio.h>
#include <string.h>

#define ALPHABET_LEN 256
#define size_t unsigned

// True if the suffix of word starting at word[pos] is 
// a prefix of word as well
bool is_prefix(const char* word, size_t w_len, size_t pos) {
    
    for (size_t ind = 0; ind < w_len - pos; ++ind)
    {
        if (word[ind] != word[pos + ind]) {
            return false;
        }
    }

    return true;
}

// length of the longest suffix of word ending at word[pos]
size_t suffix_length(const char* word, size_t w_len, size_t pos) {
    int i;
    for (i = 0; i < pos && word[w_len - 1 - i] == word[pos - i]; ++i);
    return i;
}

void compute_delta2(size_t* delta2, const char* word, size_t w_len)
{
    size_t last_prefix_ind = w_len - 1;
    for (int p = w_len - 1; p >= 0; --p) {
        if (is_prefix(word, w_len, p + 1)) {
            last_prefix_ind = p + 1;
        }
        // we shift with last_prefix_ind, but relative to the end of word
        delta2[p] = (w_len - 1 - p) + last_prefix_ind;
    }

    for (int p = 0; p < w_len; ++p) {
        size_t slen = suffix_length(word, w_len, p);
        if (word[p - slen] != word[w_len - 1 - slen]) {
            delta2[w_len - 1 - slen] = w_len - p + slen;
        }
    }
}

void compute_delta1(const char* pat, size_t pat_len, int *delta) {
    for (int i = 0; i < ALPHABET_LEN; ++i) {
        delta[i] = pat_len;
    }

    for (int j = 0; j < pat_len; ++j) {
        delta[pat[j] - '\0'] = pat_len - 1 - j;
    }
}

const char* find(const char* pat, size_t pat_len, const char* txt, size_t txt_len) {
    int delta1[ALPHABET_LEN];
    compute_delta1(pat, pat_len, delta1);

    size_t* delta2 = new size_t[pat_len];
    compute_delta2(delta2, pat, pat_len);

    int i = pat_len - 1;
    while ( i < txt_len ) {
        int j = pat_len - 1;
        while ( j >= 0 && pat[j] == txt[i]) {
            --j;
            --i;
        }
        if (j < 0) {
            delete delta2;
            return txt + i + 1;
        }
        
        // bot i and j are located at the last mismatch
        i += delta1[txt[i] - '\0'];
    }

    delete delta2;
    return NULL;
}

//int main(int argc, char* args[]) {
//    char text[100];
//    fscanf(stdin, "%100[^\n]", text);
//    int txt_len = strlen(text);
//
//    fgetc(stdin);
//
//    char pattern[100];
//    fscanf(stdin, "%100[^\n]", pattern);
//    int pat_len = strlen(pattern);
//
//    const char* pos = find(pattern, pat_len, text, txt_len);
//    int pos_ind = -1;
//    if (pos != NULL) pos_ind = pos - text + 1;
//
//    printf("Found match at position: %d\n", pos_ind);
//}